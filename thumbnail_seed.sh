#!/bin/bash
# 갤러리 게시판 더미 썸네일 데이터 생성 스크립트
# EC2에서 실행: bash thumbnail_seed.sh
# 필요: AWS CLI 설정 완료, docker 실행 중

set -e

BUCKET="${S3_BUCKET}"
REGION="${S3_REGION}"
DB_PASS="asd@1252370"
DB_NAME="potal"

if [ -z "$BUCKET" ] || [ -z "$REGION" ]; then
  echo "❌ S3_BUCKET, S3_REGION 환경 변수를 설정해주세요."
  exit 1
fi

echo "✅ S3 Bucket: $BUCKET / Region: $REGION"

# 갤러리 게시물 board_id 목록 (더미 데이터 기준 1~25)
BOARD_IDS=(1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25)

# 다양한 플레이스홀더 이미지 URL (picsum.photos - 300x300 랜덤)
PLACEHOLDER_URLS=(
  "https://picsum.photos/seed/gallery1/300/300"
  "https://picsum.photos/seed/gallery2/300/300"
  "https://picsum.photos/seed/gallery3/300/300"
  "https://picsum.photos/seed/gallery4/300/300"
  "https://picsum.photos/seed/gallery5/300/300"
  "https://picsum.photos/seed/gallery6/300/300"
  "https://picsum.photos/seed/gallery7/300/300"
  "https://picsum.photos/seed/gallery8/300/300"
  "https://picsum.photos/seed/gallery9/300/300"
  "https://picsum.photos/seed/gallery10/300/300"
)

# 기존 tb_file, tb_thumbnail 갤러리 데이터 초기화
echo "🗑️  기존 갤러리 파일/썸네일 데이터 초기화 중..."
docker exec potal-mysql mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -e "
DELETE FROM tb_thumbnail WHERE file_id IN (SELECT file_id FROM tb_file WHERE board_type='gallery');
DELETE FROM tb_file WHERE board_type='gallery';
ALTER TABLE tb_file AUTO_INCREMENT = 1;
ALTER TABLE tb_thumbnail AUTO_INCREMENT = 1;
"

FILE_ID=1
THUMBNAIL_ID=1

for i in "${!BOARD_IDS[@]}"; do
  BOARD_ID="${BOARD_IDS[$i]}"
  URL_IDX=$((i % ${#PLACEHOLDER_URLS[@]}))
  IMAGE_URL="${PLACEHOLDER_URLS[$URL_IDX]}"

  # UUID 생성
  PHYSICAL_NAME=$(cat /proc/sys/kernel/random/uuid 2>/dev/null || uuidgen | tr '[:upper:]' '[:lower:]')
  THUMB_PHYSICAL_NAME=$(cat /proc/sys/kernel/random/uuid 2>/dev/null || uuidgen | tr '[:upper:]' '[:lower:]')
  EXT="jpg"

  echo "📸 board_id=$BOARD_ID 처리 중..."

  # 이미지 다운로드
  TMP_FILE="/tmp/gallery_${BOARD_ID}.jpg"
  curl -sL "$IMAGE_URL" -o "$TMP_FILE"

  if [ ! -f "$TMP_FILE" ] || [ ! -s "$TMP_FILE" ]; then
    echo "  ⚠️  이미지 다운로드 실패, 스킵"
    continue
  fi

  FILE_SIZE=$(stat -c%s "$TMP_FILE" 2>/dev/null || stat -f%z "$TMP_FILE")

  # S3에 원본 업로드 (gallery/{uuid}.jpg)
  aws s3 cp "$TMP_FILE" "s3://$BUCKET/gallery/${PHYSICAL_NAME}.${EXT}" \
    --region "$REGION" --content-type "image/jpeg" --quiet

  # S3에 썸네일 업로드 (thumbnail/{uuid}.jpg) - 같은 이미지 사용
  aws s3 cp "$TMP_FILE" "s3://$BUCKET/thumbnail/${THUMB_PHYSICAL_NAME}.${EXT}" \
    --region "$REGION" --content-type "image/jpeg" --quiet

  # DB에 tb_file 삽입
  docker exec potal-mysql mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -e "
INSERT INTO tb_file (board_type, board_id, original_name, physical_name, file_path, extension, size, created_at)
VALUES ('gallery', $BOARD_ID, 'image.jpg', '${PHYSICAL_NAME}', '/gallery', '${EXT}', $FILE_SIZE, NOW());
"

  # 삽입된 file_id 조회
  INSERTED_FILE_ID=$(docker exec potal-mysql mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -sNe \
    "SELECT file_id FROM tb_file WHERE physical_name='${PHYSICAL_NAME}';")

  # DB에 tb_thumbnail 삽입
  docker exec potal-mysql mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -e "
INSERT INTO tb_thumbnail (file_id, original_name, physical_name, file_path, extension, size, created_at)
VALUES ($INSERTED_FILE_ID, 'image.jpg', '${THUMB_PHYSICAL_NAME}', '/thumbnail', '${EXT}', $FILE_SIZE, NOW());
"

  rm -f "$TMP_FILE"
  echo "  ✅ board_id=$BOARD_ID 완료 (file_id=$INSERTED_FILE_ID)"
done

echo ""
echo "🎉 완료! 갤러리 게시물 ${#BOARD_IDS[@]}개의 썸네일이 생성되었습니다."
