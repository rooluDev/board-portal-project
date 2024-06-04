<template>
  <Navbar/>
  <v-row>
    <v-col cols="2"></v-col>
    <v-col cols="8">
      <v-card class="card">
        <v-card-title class="board-category">자유 게시판</v-card-title>
        <v-card-subtitle style="margin-bottom: 50px">
          <v-row align="center" class="info-row">
            <v-col cols="12" class="d-flex title-container">
              <span class="category">{{ freeBoard.categoryName }}</span>
            </v-col>
            <v-col cols="12" class="d-flex justify-start align-center">
              <span class="title">{{ freeBoard.title }}</span>
            </v-col>
            <v-col cols="12" class="d-flex justify-end align-center">
              <span class="created-at" style="margin-right: 10px">{{
                  parseStringByFormat(freeBoard.createdAt, 'YYYY.MM.DD HH:mm')
                }}</span>
              <span class="author-name">{{ freeBoard.adminName || freeBoard.memberName }}</span>
              <v-spacer></v-spacer>
              <span class="views">조회수 : {{ freeBoard.views }}</span>
            </v-col>
          </v-row>
        </v-card-subtitle>
        <v-card-title class="content">{{ freeBoard.content }}</v-card-title>
        <FileList :fileList="fileList"/>
      </v-card>
      <CommentList :commentList="commentList" @submit="addComment" :isDeleted="freeBoard.isDeleted==1"/>
      <v-card-actions style="justify-content: center">
        <v-btn class="custom-btn" @click="goToList">목록</v-btn>
        <v-btn class="custom-btn" v-show="isMyBoard" @click="goToModify">수정</v-btn>
        <v-btn class="custom-btn" v-show="isMyBoard" @click="deleteBoard">삭제</v-btn>
      </v-card-actions>
    </v-col>
    <v-col cols="2"></v-col>
  </v-row>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import FileList from "@/components/FileList.vue";
import CommentList from "@/components/CommentList.vue";
import {computed, onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchAddFreeView, fetchCheckFreeAuthor, fetchDeleteFreeBoard, fetchGetFreeBoard} from "@/api/freeBoardService";
import {fetchAddComment} from "@/api/commentService";
import {useStore} from "vuex";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {Board} from "@/type/boardType";

export default {
  methods: {parseStringByFormat},
  components: {Navbar, FileList, CommentList},
  setup() {
    const freeBoard = ref({});
    const fileList = ref([]);
    const commentList = ref([]);
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;
    const store = useStore();
    const isMyBoard = ref(false);
    const accessToken = computed(() => store.getters.getAccessToken);

    /**
     * 게시판에 필요한 데이터 로드
     */
    const getBoard = async () => {
      const res = await fetchGetFreeBoard(boardId);
      await fetchAddFreeView(boardId);
      freeBoard.value = res.freeBoard;
      commentList.value = res.commentList;
      fileList.value = res.fileList;
    }

    /**
     * 작성자인지 확인
     */
    const checkAuthor = async () => {
      try {
        await fetchCheckFreeAuthor(boardId);
        isMyBoard.value = true;
      } catch (error) {
        isMyBoard.value = false;
      }
    }

    onMounted(() => {
      getBoard();
      checkAuthor();
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
        commentList.value = await fetchAddComment(content, boardId, Board.FREE_BOARD);
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
      if (!confirm("삭제 하시겠습니까?")){
        return;
      }
      try {
        await fetchDeleteFreeBoard(boardId);
        goToList();
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    const goToModify = () => {
      router.push({
        name: 'Free-Modify',
        query: route.query
      })
    }

    const goToList = () => {
      router.push({
        name: 'Free-List',
        query: route.query
      })
    }


    return {
      freeBoard,
      fileList,
      commentList,
      isMyBoard,
      goToList,
      addComment,
      deleteBoard,
      goToModify
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
  min-height: 48px; /* Ensure there is enough height for multiline titles */
}

.title {
  font-weight: 500;
  font-size: 24px;
  max-width: 100%; /* Ensure the title does not overflow the container */
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