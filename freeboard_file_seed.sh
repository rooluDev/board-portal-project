#!/bin/bash
# 자유게시판 더미 첨부파일 데이터 생성 스크립트
# EC2에서 실행: sudo -E bash freeboard_file_seed.sh
# 필요: AWS CLI 설정 완료, docker 실행 중

set -e

BUCKET="${S3_BUCKET}"
REGION="${S3_REGION}"
DB_PASS="asd@1252370"
DB_NAME="potal"
CONTAINER="mysql"

if [ -z "$BUCKET" ] || [ -z "$REGION" ]; then
  echo "❌ S3_BUCKET, S3_REGION 환경 변수를 설정해주세요."
  exit 1
fi

echo "✅ S3 Bucket: $BUCKET / Region: $REGION"

# 파일 첨부할 board_id 목록 (25개 중 랜덤하게 13개 선택)
BOARD_IDS_WITH_FILE=(2 4 5 7 9 10 12 14 16 18 20 22 24)

# 일부 게시물은 파일 2개 첨부
BOARD_IDS_MULTI_FILE=(5 10 16 22)

# 다양한 플레이스홀더 이미지 URL
PLACEHOLDER_URLS=(
  "https://picsum.photos/seed/free1/800/600"
  "https://picsum.photos/seed/free2/800/600"
  "https://picsum.photos/seed/free3/800/600"
  "https://picsum.photos/seed/free4/800/600"
  "https://picsum.photos/seed/free5/800/600"
  "https://picsum.photos/seed/free6/800/600"
  "https://picsum.photos/seed/free7/800/600"
  "https://picsum.photos/seed/free8/800/600"
)

FILE_NAMES=(
  "사진1.jpg"
  "이미지.jpg"
  "첨부사진.jpg"
  "photo.jpg"
  "image.jpg"
  "스크린샷.jpg"
  "파일.jpg"
  "사진모음.jpg"
)

# 기존 자유게시판 파일 데이터 초기화
echo "🗑️  기존 자유게시판 파일 데이터 초기화 중..."
docker exec $CONTAINER mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -e "
DELETE FROM tb_file WHERE board_type='free';
"

upload_file() {
  local BOARD_ID=$1
  local URL_IDX=$2
  local FILE_NAME_IDX=$3

  IMAGE_URL="${PLACEHOLDER_URLS[$((URL_IDX % ${#PLACEHOLDER_URLS[@]}))]}"
  ORIGINAL_NAME="${FILE_NAMES[$((FILE_NAME_IDX % ${#FILE_NAMES[@]}))]}"
  PHYSICAL_NAME=$(cat /proc/sys/kernel/random/uuid 2>/dev/null || uuidgen | tr '[:upper:]' '[:lower:]')
  EXT="jpg"

  TMP_FILE="/tmp/free_${BOARD_ID}_${URL_IDX}.jpg"
  curl -sL "$IMAGE_URL" -o "$TMP_FILE"

  if [ ! -f "$TMP_FILE" ] || [ ! -s "$TMP_FILE" ]; then
    echo "  ⚠️  이미지 다운로드 실패"
    return
  fi

  FILE_SIZE=$(stat -c%s "$TMP_FILE" 2>/dev/null || stat -f%z "$TMP_FILE")

  # S3에 업로드 (free/{uuid}.jpg)
  aws s3 cp "$TMP_FILE" "s3://$BUCKET/free/${PHYSICAL_NAME}.${EXT}" \
    --region "$REGION" --content-type "image/jpeg" --quiet

  # DB에 tb_file 삽입
  docker exec $CONTAINER mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -e "
INSERT INTO tb_file (board_type, board_id, original_name, physical_name, file_path, extension, size, created_at)
VALUES ('free', $BOARD_ID, '${ORIGINAL_NAME}', '${PHYSICAL_NAME}', '/free', '${EXT}', $FILE_SIZE, NOW());
"

  rm -f "$TMP_FILE"
  echo "  ✅ 파일 업로드 완료: $ORIGINAL_NAME (board_id=$BOARD_ID)"
}

URL_IDX=0
for BOARD_ID in "${BOARD_IDS_WITH_FILE[@]}"; do
  echo "📎 board_id=$BOARD_ID 처리 중..."

  upload_file $BOARD_ID $URL_IDX $URL_IDX

  # 일부 게시물은 파일 2개 첨부
  if [[ " ${BOARD_IDS_MULTI_FILE[@]} " =~ " ${BOARD_ID} " ]]; then
    URL_IDX=$((URL_IDX + 1))
    upload_file $BOARD_ID $URL_IDX $((URL_IDX + 3))
    echo "  📎 두 번째 파일 추가 (board_id=$BOARD_ID)"
  fi

  URL_IDX=$((URL_IDX + 1))
done

echo ""
echo "🎉 완료! 자유게시판 첨부파일 데이터가 생성되었습니다."
docker exec $CONTAINER mysql -u root -p"$DB_PASS" --default-character-set=utf8mb4 "$DB_NAME" -e "
SELECT board_id, original_name, extension, size FROM tb_file WHERE board_type='free' ORDER BY board_id;
"
