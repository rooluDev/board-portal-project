<template>
  <Navbar/>
  <InquiryBoardForm :editing="true" :inputForm="inquiryBoard" @submit="modifyBoard" @cancel="goToList"/>
</template>

<script>
import store from "@/store";
import Navbar from "@/components/Navbar.vue";
import InquiryBoardForm from "@/components/InquiryBoardForm.vue";
import {computed, onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {
  fetchCheckInquiryAuthor,
  fetchGetInquiryBoard,
  fetchModifyInquiryBoard
} from "@/api/inquiryBoardService";
import {inquiryBoardValidator} from "@/validator/validator";

export default {
  components: {Navbar, InquiryBoardForm},
  setup() {
    const inquiryBoard = ref({});
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;
    const accessToken = computed(() => store.getters.getAccessToken);


    watch(accessToken, (newToken) => {
      if (!newToken) {
        alert("로그인이 필요합니다.");
        goToList();
      } else {
        try {
          fetchCheckInquiryAuthor(boardId);
        } catch (error) {
          alert("로그인이 필요합니다.");
          goToList();
        }
      }
    })

    /**
     * 게시판 데이터 가져오기
     */
    const getBoard = async () => {
      try {
        const res = await fetchGetInquiryBoard(boardId);
        inquiryBoard.value = res.inquiryBoard;
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    onMounted(() => {
      getBoard();
    })

    /**
     * 게시판 수정
     */
    const modifyBoard = async () => {
      try {
        inquiryBoardValidator(inquiryBoard.value);
      } catch (error) {
        alert(error.message);
        return;
      }

      try {
        await fetchModifyInquiryBoard(inquiryBoard.value);
        alert("수정 되었습니다.");
        goToList();
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    const goToList = () => {
      router.push({
        name: 'Inquiry-List',
        query: route.query
      })
    }

    return {
      inquiryBoard,
      goToList,
      modifyBoard
    }
  }
}
</script>

<style scoped>

</style>