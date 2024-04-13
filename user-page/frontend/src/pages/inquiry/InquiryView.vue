<template>
  <Navbar/>
  <hr/>
  <Board :board="board">
    <template v-slot:boardName>
      <h1>문의 게시판</h1>
    </template>
    <template v-slot:answer>
      <span v-if="board.answerId != null">( 답변완료 )</span>
      <span v-else>( 미답변 )</span>
    </template>
  </Board>
  <div v-if="answer.answerId">
    <span>{{ answer.authorName }}</span>
    <span>{{ parseStringByFormat(answer.createdAt, 'YYYY.MM.DD HH:mm') }}</span>
    <hr/>
    <span>{{ answer.content }}</span>
  </div>
  <div v-else>
    <span>아직 등록된 답변이 없습니다.</span>
  </div>
  <button type="button" @click="goToList">목록</button>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import Board from "@/components/Board.vue";
import {fetchInquiryBoard} from "@/api/inquiryBoardService";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {parseStringByFormat} from "@/utils/SearchConditionUtils";
import {useStore} from "vuex";

export default {
  components: {Navbar, Board},
  setup() {
    const board = ref({});
    const answer = ref({});
    const route = useRoute();
    const router = useRouter();
    const store = useStore();
    const boardId = route.params.id;
    const accessToken = ref();
    accessToken.value = store.getters.getAccessToken;

    const searchCondition = {
      startDate: route.query.startDate,
      endDate: route.query.endDate,
      searchText: route.query.searchText,
      pageSize: route.query.pageSize,
      orderValue: route.query.orderValue,
      orderDirection: route.query.orderDirection,
      pageNum: route.query.pageNum
    }

    const getBoard = async () => {
        const res = await fetchInquiryBoard(boardId, accessToken.value);
        board.value = res.inquiryBoard;
        answer.value = res.answer;
    }
    getBoard();

    const goToList = () => {
      router.push({
        name: 'Inquiry-List',
        query: searchCondition
      })
    }
    return {
      board,
      answer,
      goToList,
      parseStringByFormat
    }
  }
}
</script>

<style scoped>

</style>