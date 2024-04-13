<template>
  <Navbar/>
  <hr/>
  <Board :board="board">
    <template v-slot:boardName>
      <h1>공지사항</h1>
    </template>
    <template v-slot:category>
      <span>{{ board.categoryName }}</span>
    </template>
  </Board>
  <button type="button" @click="goToList">목록</button>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import Board from "@/components/Board.vue";
import {fetchNoticeBoard} from "@/api/noticeBoardService";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";

export default {
  components: {Navbar, Board},
  setup() {
    const board = ref({});
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;

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
      board.value = await fetchNoticeBoard(boardId);
    }
    getBoard();

    const goToList = () => {
      router.push({
        name: 'Notice-List',
        query: searchCondition
      })
    }

    return {
      board,
      goToList
    }
  }
}
</script>

<style scoped>

</style>