<template>
  <Navbar/>
  <hr/>
  <h1>갤러리</h1>
  <SearchForm :categoryList="categoryList" :searchCondition="searchCondition" @search="getGalleryBoardList">
    <template v-slot:category>
      <select v-model="searchCondition.category">
        <option value="-1">전체 분류</option>
        <option v-for="category in categoryList" :value="category.categoryId" :key="category.categoryId">
          {{ category.categoryName }}
        </option>
      </select>
    </template>
    <template v-slot:searchBar>
      <input type="text" placeholder="제목 or 내용" v-model="searchCondition.searchText">
    </template>
    <template v-slot:orderValue>
      <select v-model="searchCondition.orderValue">
        <option value="createdAt">등록 일시</option>
        <option value="category">분류</option>
        <option value="title">제목</option>
        <option value="views">조회수</option>
      </select>
    </template>
  </SearchForm>
  <button type="button" @click="goToWrite">글 등록</button>
  <BoardList :boardList="galleryBoardList">
    <template v-slot:td="{item}">
      <div>
        <router-link :to="{name:'Gallery-View',params:{id:item.boardId}}">
          <img :src="`${item.filePath}/${item.physicalName}.${item.extension}`"/>
        </router-link>
        <div>
          <router-link :to="{name:'Gallery-View',params:{id:item.boardId}}">
            {{ item.title }}
          </router-link>
          <span v-if="isNew(item.createdAt,7)"> new</span>
          <br/>
          <span>{{ item.content }}</span>
        </div>
        <hr/>
      </div>
    </template>
  </BoardList>
  <Pagination :totalPageNum="totalPageNum" @click="getGalleryBoardList"/>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import SearchForm from "@/components/SearchForm.vue";
import Pagination from "@/components/Pagination.vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {ref} from "vue";
import {fetchGalleryBoardList} from "@/api/galleryBoardService";
import router from "@/router";
import {parseStringByFormat} from "@/utils/SearchConditionUtils";
import BoardList from "@/components/BoardList.vue";
import {isNew} from "@/utils/DateUtils";

export default {
  components: {BoardList, Navbar, SearchForm, Pagination},
  setup() {
    const store = useStore();
    const route = useRoute();
    const accessToken = ref();
    accessToken.value = store.getters.getAccessToken;

    const galleryBoardList = ref([]);
    const totalPageNum = ref(0);
    const categoryList = ref([]);

    const searchCondition = ref({
      startDate: route.query.startDate || '',
      endDate: route.query.endDate || '',
      category: route.query.category || -1,
      searchText: route.query.searchText || '',
      pageSize: route.query.pageSize || 10,
      orderValue: route.query.orderValue || 'createdAt',
      orderDirection: route.query.orderDirection || 'desc',
      pageNum: route.query.pageNum || 1
    });

    const getGalleryBoardList = async (pageNum) => {
      searchCondition.value.pageNum = pageNum;
      const res = await fetchGalleryBoardList(searchCondition.value);
      categoryList.value = res.categoryList;
      totalPageNum.value = res.totalPageNum;
      searchCondition.value = res.searchCondition;
      galleryBoardList.value = res.galleryBoardList;
    }

    getGalleryBoardList(searchCondition.value.pageNum);

    const goToView = (boardId) => {
      router.push({
        name: 'Gallery-View',
        params: {
          id: boardId
        },
        query: {
          startDate: parseStringByFormat(searchCondition.value.startDate, 'YYYY-MM-DD'),
          endDate: parseStringByFormat(searchCondition.value.endDate, 'YYYY-MM-DD'),
          searchText: searchCondition.value.searchText,
          pageSize: searchCondition.value.pageSize,
          orderValue: searchCondition.value.orderValue,
          orderDirection: searchCondition.value.orderDirection,
          pageNum: searchCondition.value.pageNum
        }
      })
    }

    const goToWrite = () => {
      console.log(accessToken.value);
      if (accessToken.value) {
        router.push({
          name: 'Gallery-Write',
          query: {
            startDate: parseStringByFormat(searchCondition.value.startDate, 'YYYY-MM-DD'),
            endDate: parseStringByFormat(searchCondition.value.endDate, 'YYYY-MM-DD'),
            searchText: searchCondition.value.searchText,
            pageSize: searchCondition.value.pageSize,
            orderValue: searchCondition.value.orderValue,
            orderDirection: searchCondition.value.orderDirection,
            pageNum: searchCondition.value.pageNum
          }
        })
      } else {
        router.push({
          name:'Login'
        })
      }
    }

    return {
      searchCondition,
      totalPageNum,
      categoryList,
      galleryBoardList,
      getGalleryBoardList,
      parseStringByFormat,
      goToView,
      goToWrite,
      isNew
    }
  }
}
</script>

<style scoped>

</style>