<template>
  <Navbar/>
  <h1>갤러리</h1>
  <BoardInputForm :editing="true" :inputForm="board" :categoryList="categoryList" :fileList="fileList"
                  @cancel="goToList">
    <template v-slot:category>
      <label>분류*</label>
      <select v-model="board.categoryId">
        <option value="-1">전체 분류</option>
        <option v-for="category in categoryList" :value="category.categoryId" :key="category.categoryId">
          {{ category.categoryName }}
        </option>
      </select>
      <br/>
    </template>
    <template v-slot:file>
      <label>갤러리 이미지</label>
      <div v-for="(file,index) in fileList" :key="index">
        <img :src="`${file.filePath}/${file.physicalName}.${file.extension}`"/>
        <input type="file">
        <button type="button">X</button>
        <span>{{ file.originalName }}</span>
      </div>
    </template>
  </BoardInputForm>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import BoardInputForm from "@/components/BoardInputForm.vue";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchGalleryBoard} from "@/api/galleryBoardService";
import store from "@/store";

export default {
  components: {Navbar, BoardInputForm},
  setup() {
    const board = ref({});
    const categoryList = ref([]);
    const fileList = ref([]);
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
      fileList.value = res.fileList;
      categoryList.value = res.categryList;
    }
    getBoard();

    const goToList = () => {
      router.push({
        name: 'Gallery-List',
        query: searchCondition
      })
    }

    return {
      board,
      categoryList,
      fileList,
      getBoard,
      goToList
    }
  }
}
</script>

<style scoped>

</style>