<template>
  <Navbar/>
  <h1>자유 게시판</h1>
  <BoardInputForm :editing="true"
                  :inputForm="board"
                  :categoryList="categoryList"
                  :fileList="fileList"
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
      <div v-for="(file,index) in fileList" :key="index">
        <span>{{ file.originalName }}</span>
        <input type="file">
        <button type="button">X</button>
      </div>
    </template>
  </BoardInputForm>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import BoardInputForm from "@/components/BoardInputForm.vue";
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";
import {fetchFreeBoard} from "@/api/freeBoardService";

export default {
  components: {Navbar, BoardInputForm},
  setup() {
    const board = ref({});
    const categoryList = ref([]);
    const fileList = ref([]);
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
      fileList.value = res.fileList;
      categoryList.value = res.categoryList;
      console.log(res);
    }
    getBoard();

    const goToList = () => {
      router.push({
        name: 'Free-List',
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