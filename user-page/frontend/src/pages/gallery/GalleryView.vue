<template>
  <Navbar/>
  <v-row>
    <v-col cols="2"></v-col>
    <v-col cols="8">
      <v-card class="card">
        <v-card-title class="board-category">갤러리</v-card-title>
        <v-card-subtitle style="margin-bottom: 50px">
          <v-row align="center" class="info-row">
            <v-col cols="12" class="d-flex title-container">
              <span class="category">{{ galleryBoard.categoryName }}</span>
            </v-col>
            <v-col cols="12" class="d-flex justify-start align-center">
              <span class="title">{{ galleryBoard.title }}</span>
            </v-col>
            <v-col cols="12" class="d-flex justify-end align-center">
              <span class="created-at" style="margin-right: 10px">{{
                  parseStringByFormat(galleryBoard.createdAt, 'YYYY.MM.DD HH:mm')
                }}</span>
              <span class="author-name">{{ galleryBoard.adminName || galleryBoard.memberName }}</span>
              <v-spacer></v-spacer>
              <span class="views">조회수 : {{ galleryBoard.views }}</span>
            </v-col>
          </v-row>
        </v-card-subtitle>
        <v-carousel class="mb-12" v-if="imageUrls.length">
          <v-carousel-item
              v-for="(url,index) in imageUrls"
              :key="index"
              :src="url"
          >
          </v-carousel-item>
        </v-carousel>
        <v-card-title class="content">{{ galleryBoard.content }}</v-card-title>
        <FileList :fileList="fileList"/>
        <CommentList :commentList="commentList" @submit="addComment" :isDeleted="galleryBoard.isDeleted === 1"/>
        <v-card-actions style="justify-content: center">
          <v-btn class="custom-btn" @click="goToList">목록</v-btn>
          <v-btn class="custom-btn" v-if="galleryBoard.isDeleted !== 1" @click="goToModify" v-show="isMyBoard">수정
          </v-btn>
          <v-btn class="custom-btn" v-if="galleryBoard.isDeleted !== 1" @click="deleteBoard" v-show="isMyBoard">삭제
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-col>
    <v-col cols="2"></v-col>
  </v-row>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import CommentList from "@/components/CommentList.vue";
import store from "@/store";
import {computed, onMounted, ref, watch} from "vue";
import FileList from "@/components/FileList.vue";
import {useRoute, useRouter} from "vue-router";
import {fetchAddComment} from "@/api/commentService";
import {fetchGetFileResource} from "@/api/imgaeService";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {
  fetchAddGalleryView,
  fetchGetGalleryBoard,
  fetchCheckGalleryAuthor,
  fetchDeleteGalleryBoard
} from "@/api/galleryBoardService";
import {Board} from "@/type/boardType";

export default {
  components: {Navbar, CommentList, FileList},
  setup() {
    const route = useRoute();
    const router = useRouter();
    const galleryBoard = ref({});
    const fileList = ref([]);
    const commentList = ref([]);
    const boardId = route.params.id;
    const imageUrls = ref([]);
    const isMyBoard = ref(false);
    const accessToken = computed(() => store.getters.getAccessToken);

    /**
     * 게시판에 필요한 데이터 로드
     */
    const getBoard = async () => {
      const res = await fetchGetGalleryBoard(boardId);
      await fetchAddGalleryView(boardId);
      galleryBoard.value = res.galleryBoard;
      commentList.value = res.commentList;
      fileList.value = res.fileList;
    }


    /**
     * 게시판의 이미지 파일 리소스 로드
     */
    const getFileResource = async () => {
      for (const file of fileList.value) {
        const index = fileList.value.indexOf(file);
        const imageBlob = await fetchGetFileResource(file.fileId);
        imageUrls.value[index] = URL.createObjectURL(imageBlob);
      }
    };

    /**
     * 작성자인지 확인
     */
    const checkAuthor = async () => {
      try {
        await fetchCheckGalleryAuthor(boardId);
        isMyBoard.value = true;
      } catch (error) {
        isMyBoard.value = false;
      }
    }

    onMounted(async () => {
      await getBoard();
      await getFileResource();
      await checkAuthor();
    })

    watch(accessToken, () => {
      checkAuthor();
    })

    /**
     * 댓글 추가
     *
     * @param content 내용
     */
    const addComment = async (content) => {
      try {
        const res = await fetchAddComment(content, boardId, Board.GALLERY_BOARD);
        // commentList reload
        commentList.value = res;
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    /**
     * 게시물 삭제
     */
    const deleteBoard = async () => {
      try {
        if (!confirm("삭제 하시겠습니까?")) {
          return;
        }
        await fetchDeleteGalleryBoard(boardId);
        goToList();
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    const goToModify = () => {
      router.push({
        name: 'Gallery-Modify',
        query: route.query
      })
    }

    const goToList = () => {
      router.push({
        name: 'Gallery-List',
        query: route.query
      })
    }

    return {
      galleryBoard,
      fileList,
      commentList,
      isMyBoard,
      imageUrls,
      goToList,
      addComment,
      goToModify,
      deleteBoard,
      parseStringByFormat
    }
  }
}
</script>

<style scoped>
.card {
  margin-top: 70px;
  margin-bottom: 10px;
  padding-top: 10px;
  padding-left: 20px;
  padding-right: 20px;
}

.board-category {
  font-size: 40px;
}

.created-at, .author-name, .views {
  font-size: 16px;
  font-weight: 400;
}

.category {
  font-size: 25px;
  font-weight: 500;
  color: black;
}

.title-container {
  min-height: 48px;
}

.title {
  font-weight: 500;
  font-size: 24px;
  max-width: 100%;
  white-space: pre-wrap;
}

.info-row {
  display: flex;
  align-items: center;
}

.content {
  white-space: pre-wrap;
  margin-bottom: 20px;
  border: 1px solid black;
  padding: 15px;
}

.custom-btn {
  margin-top: 30px;
  margin-bottom: 20px;
  background-color: black;
  color: white;
  width: 200px;
}
</style>