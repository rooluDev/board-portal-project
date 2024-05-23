<template>
  <Navbar/>
  <InquiryBoardForm :editing="false" :inputForm="inquiryBoardForm" @submit="writeBoard" @cancel="goToList"/>
</template>

<script>
import InquiryBoardForm from "@/components/InquiryBoardForm.vue";
import Navbar from "@/components/Navbar.vue";
import {computed, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";
import {fetchAddInquiryBoard} from "@/api/inquiryBoardService";
import {inquiryBoardValidator} from "@/validator/validator";

export default {
  components: {InquiryBoardForm, Navbar},
  setup() {
    const store = useStore();
    const accessToken = computed(() => store.getters.getAccessToken);
    const route = useRoute();
    const router = useRouter();
    const inquiryBoardForm = ref({
      title: '',
      content: '',
      isSecret: ''
    });

    const constraint = {
      title: {
        maxLength: 99,
        minLength: 1
      },
      content: {
        maxLength: 3999,
        minLength: 1
      }
    }

    /**
     * 문의게시판 추가
     */
    watch(accessToken, (newToken) => {
      if (!newToken) {
        alert("로그인이 필요합니다.");
        goToList();
      }
    })

    /**
     * 문의 게시판 작성
     */
    const writeBoard = async () => {
      try {
        inquiryBoardValidator(inquiryBoardForm.value, constraint);
        await fetchAddInquiryBoard(inquiryBoardForm.value);
        await router.push({
          name: 'Inquiry-List'
        })
      } catch (error) {
        alert(error.message);
      }
    }

    const goToList = () => {
      router.push({
        name: 'Inquiry-List',
        query: route.query
      })
    }

    return {
      inquiryBoardForm,
      writeBoard,
      goToList
    }
  }
}
</script>

<style scoped>

</style>