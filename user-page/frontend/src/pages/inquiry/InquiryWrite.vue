<template>
  <Navbar/>
  <h1>문의 게시판</h1>
  <BoardInputForm :editing="false" :inputForm="inputForm" @submit="writeBoard" @cancel="goToList">
    <template v-slot:secret>
      <label>비밀글</label>
      <input type="checkbox" v-model="inputForm.secret">
    </template>
  </BoardInputForm>
</template>

<script>
import BoardInputForm from "@/components/BoardInputForm.vue";
import Navbar from "@/components/Navbar.vue";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";
import {fetchAddInquiryBoard} from "@/api/inquiryBoardService";

export default {
  components: {BoardInputForm, Navbar},
  setup() {
    const store = useStore();
    const accessToken = ref(null);
    accessToken.value = store.getters.getAccessToken;

    const route = useRoute();
    const router = useRouter();
    const inputForm = ref({
      title: '',
      content: '',
      secret: ''
    });

    const searchCondition = {
      startDate: route.query.startDate,
      endDate: route.query.endDate,
      searchText: route.query.searchText,
      pageSize: route.query.pageSize,
      orderValue: route.query.orderValue,
      orderDirection: route.query.orderDirection,
      pageNum: route.query.pageNum
    }

    //{title: 'asdfa', content: 'asdf', secret: true}

    const writeBoard = async () => {
      try {
        await fetchAddInquiryBoard(inputForm.value, accessToken.value);
        await router.push({
          name: 'Inquiry-List'
        })
      } catch (error) {
        alert("error");
      }
    }

    const goToList = () => {
      router.push({
        name: 'Inquiry-List',
        query: searchCondition
      })
    }

    return {
      inputForm,
      writeBoard,
      goToList
    }
  }
}
</script>

<style scoped>

</style>