<template>
  <Navbar/>
  <hr/>
  <Board :board="board">
    <template v-slot:boardName>
      <h1>자유게시판</h1>
    </template>
    <template v-slot:category>
      <span>{{ board.categoryName }}</span>
    </template>
  </Board>
  <FileList :fileList="fileList"/>
  <CommentList :commentList="commentList" @submit="addComment"/>
  <button type="button" @click="goToList">목록</button>
  <button type="button" @click="goToModify">수정</button>
  <button type="button" @click="deleteBoard">삭제</button>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import Board from "@/components/Board.vue";
import FileList from "@/components/FileList.vue";
import CommentList from "@/components/CommentList.vue";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchDeleteBoard, fetchFreeBoard} from "@/api/freeBoardService";
import {fetchComment} from "@/api/commentService";
import {useStore} from "vuex";

export default {
  components:{Navbar, Board, FileList, CommentList},
  setup(){
    const board = ref({});
    const fileList = ref([]);
    const commentList = ref([]);
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;
    const store = useStore();
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
      const res = await fetchFreeBoard(boardId);
      board.value = res.freeBoard;
      commentList.value = res.commentList;
      fileList.value = res.fileList;
    }
    getBoard();

    const addComment = async (content) => {
      const res = await fetchComment(content, boardId, 'free' ,accessToken.value);
      // commentList reload
      commentList.value = res;
    }

    const deleteBoard = async () => {
      await fetchDeleteBoard(boardId);
      goToList();
    }

    const goToModify = () => {
      router.push({
        name:'Free-Modify',
        query: searchCondition
      })
    }

    const goToList = () => {
      router.push({
        name: 'Free-List',
        query: searchCondition
      })
    }


    return{
      board,
      fileList,
      commentList,
      goToList,
      addComment,
      deleteBoard,
      goToModify
    }
  }
}
</script>

<style scoped>

</style>