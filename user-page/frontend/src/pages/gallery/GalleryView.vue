<template>
  <Navbar/>
  <hr/>
  <Board :board="board">
    <template v-slot:boardName>
      <h1>갤러리</h1>
    </template>
    <template v-slot:category>
      <span>{{ board.categoryName }}</span>
    </template>
    <template v-slot:img>

    </template>
  </Board>
  <CommentList :commentList="commentList" @submit="addComment"/>
  <button type="button" @click="goToList">목록</button>
  <button type="button" @click="goToModify">수정</button>
  <button type="button" @click="deleteBoard">삭제</button>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import Board from "@/components/Board.vue";
import CommentList from "@/components/CommentList.vue";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchComment} from "@/api/commentService";
import {fetchGalleryBoard} from "@/api/galleryBoardService";
import store from "@/store";

export default {
  components: {Navbar, Board, CommentList},
  setup() {
    const board = ref({});
    const fileList = ref([]);
    const commentList = ref([]);
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;
    const accessToken = ref();
    accessToken.value = store.getters.getAccessToken;

    const searchCondition = {
      startDate: route.query.startDate,
      endDate: route.query.endDate,
      category: route.query.category,
      searchText: route.query.searchText,
      pageSize: route.query.pageSize,
      orderValue: route.query.orderValue,
      orderDirection: route.query.orderDirection,
      pageNum: route.query.pageNum
    }

    const getBoard = async () => {
      const res = await fetchGalleryBoard(boardId);
      board.value = res.galleryBoard;
      commentList.value = res.commentList;
      fileList.value = res.fileList;
    }
    getBoard();

    const addComment = async (content) => {
      const res = await fetchComment(content, boardId, 'gallery', accessToken.value);
      // commentList reload
      commentList.value = res;
    }

    const deleteBoard = () => {
      goToList();
    }

    const goToModify = () => {
      router.push({
        name: 'Gallery-Modify',
        query: searchCondition
      })
    }

    const goToList = () => {
      router.push({
        name: 'Gallery-List',
        query: searchCondition
      })
    }

    return {
      board,
      fileList,
      commentList,
      goToList,
      addComment,
      goToModify,
      deleteBoard
    }
  }
}
</script>

<style scoped>

</style>