<template>
  <Navbar/>
  <h1>문의 게시판</h1>
  <BoardInputForm :editing="true" :inputForm="board" @submit="submitBoard" @cancel="goToList">
    <template v-slot:secret>
      <label>비밀글</label>
      <input type="checkbox" :checked="board.secret == 1">
    </template>
  </BoardInputForm>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import BoardInputForm from "@/components/BoardInputForm.vue";
import {fetchInquiryBoard} from "@/api/inquiryBoardService";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";

export default {
  components:{Navbar,BoardInputForm},
  setup(){
    const board = ref({});
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;

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
      const res = await fetchInquiryBoard(boardId);
      board.value = res.inquiryBoard;
    }
    getBoard();

    const submitBoard = () =>{

    }

    const goToList = () => {
      router.push({
        name: 'Inquiry-List',
        query: searchCondition
      })
    }
    
    return{
      board,
      goToList,
      submitBoard
    }
  }
}
</script>

<style scoped>

</style>