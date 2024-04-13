<template>
  <Navbar/>
  <h1>자유게시판</h1>
  <BoardInputForm :inputForm="inputForm" :categoryList="categoryList" :editing="false" @submit="writeBoard" @cancel="goToList">
    <template v-slot:category>
      <label>분류*</label>
      <select v-model="inputForm.categoryId">
        <option value="-1">분류 선택</option>
        <option v-for="category in categoryList" :key="category.categoryId" :value="category.categoryId">
          {{category.categoryName}}
        </option>
      </select>
      <br/>
    </template>
    <template v-slot:file>
      <label>첨부</label>
      <div v-for="(fileInput,index) in inputForm.fileList" :key="index">
        <input type="file" @change="fileSelected($event, index)" ref="fileInputRef">
        <button type="button" @click="removeFileInput(index)">X</button>
      </div>
      <button type="button" @click="addFileInput">추가</button>
    </template>
  </BoardInputForm>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import BoardInputForm from "@/components/BoardInputForm.vue";
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import {fetchCategoryListByBoardType} from "@/api/categoryService";
import store from "@/store";
import {fetchAddFreeBoard} from "@/api/freeBoardService";

export default {
  components: {BoardInputForm, Navbar},
  setup() {
    const route = useRoute();
    const router = useRouter();
    const inputForm = ref({
      categoryId: -1,
      title: '',
      content: '',
      fileList: []
    })

    const categoryList = ref([]);

    const accessToken = ref('');
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

    const getCategoryList = async () => {
      const res = await fetchCategoryListByBoardType('free');
      categoryList.value = res;
    }
    getCategoryList();

    const writeBoard = async (board) => {

      console.log(inputForm.value)
      const formData = new FormData();
      inputForm.value.fileList.forEach((file) => {
        formData.append('file', file[0]);
      })
      formData.append('categoryId',inputForm.value.categoryId);
      formData.append('title',inputForm.value.title);
      formData.append('content',inputForm.value.content);

      await fetchAddFreeBoard(formData, accessToken.value);
      await router.push({
        name:'Free-List'
      })
    }


    const addFileInput = () => {
      inputForm.value.fileList.push(null);
    }

    const fileSelected = (event, index) => {
      // console.log(event.target.files);
      // console.log(inputForm.value.fileList);
      inputForm.value.fileList[index] = event.target.files;
      console.log(inputForm.value.fileList);
    }

    const removeFileInput = (index) => {
      inputForm.value.fileList.splice(index, 1);
    }

    const goToList = () => {
      if (confirm('작성을 취소하시겠습니까?')){
        router.push({
          name: 'Free-List',
          query: searchCondition
        })
      }
    }


    return {
      inputForm,
      categoryList,
      writeBoard,
      goToList,
      addFileInput,
      fileSelected,
      removeFileInput
    }
  }
}
</script>

<style scoped>

</style>