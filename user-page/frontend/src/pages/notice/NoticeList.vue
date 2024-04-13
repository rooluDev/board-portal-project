<template>
  <Navbar/>
  <hr/>
  <h1>공지사항</h1>
  <SearchForm :categoryList="categoryList" :searchCondition="searchCondition" @search="getNoticeBoardList">
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
  <BoardList :boardList="noticeBoardList" :fixedNoticeBoardList="fixedNoticeBoardList">
    <template v-slot:th>
      <th>번호</th>
      <th>분류</th>
      <th>제목</th>
      <th>조회</th>
      <th>등록일시</th>
      <th>등록자</th>
    </template>
    <template v-slot:fixedTd="{item}">
      <td></td>
      <td>
        <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{ item.categoryName }}</router-link>
      </td>
      <td>
        <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{truncateTitle(item.title, 5) }}</router-link>
        <span v-if="isNew(item.createdAt, 7)"> new</span>
      </td>
      <td>{{ item.views }}</td>
      <td>{{ parseStringByFormat(item.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
      <td>{{ item.authorName }}</td>
    </template>
    <template v-slot:td="{ item }">
      <td>
        <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
      </td>
      <td>
        <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{ item.categoryName }}</router-link>
      </td>
      <td>
        <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{truncateTitle(item.title, 5) }}</router-link>
        <span v-if="isNew(item.createdAt, 7)"> new</span>
      </td>
      <td>{{ item.views }}</td>
      <td>{{ parseStringByFormat(item.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
      <td>{{ item.authorName }}</td>
    </template>
  </BoardList>
  <Pagination :totalPageNum="totalPageNum" @click="getNoticeBoardList"/>
</template>

<script>
import {useStore} from "vuex";
import Navbar from "@/components/Navbar.vue";
import SearchForm from "@/components/SearchForm.vue";
import BoardList from "@/components/BoardList.vue";
import Pagination from "@/components/Pagination.vue";
import {fetchNoticeBoardList} from "@/api/noticeBoardService";
import {ref} from "vue";
import {useRoute} from "vue-router";
import router from "@/router";
import {parseStringByFormat} from "@/utils/SearchConditionUtils";
import {isNew} from "@/utils/DateUtils";
import {truncateTitle} from "@/utils/StringUtils";

export default {
  components: {Navbar, SearchForm, BoardList, Pagination},
  setup() {
    const store = useStore();
    const route = useRoute();
    const accessToken = ref();
    accessToken.value = store.getters.getAccessToken;

    const noticeBoardList = ref([]);
    const fixedNoticeBoardList = ref([]);
    const categoryList = ref([]);
    const totalPageNum = ref(0);
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

    const getNoticeBoardList = async (pageNum) => {
      searchCondition.value.pageNum = pageNum;
      const res = await fetchNoticeBoardList(searchCondition.value);
      categoryList.value = res.categoryList;
      searchCondition.value = res.searchCondition;
      noticeBoardList.value = res.noticeBoardList;
      fixedNoticeBoardList.value = res.fixedBoardList;
      totalPageNum.value = res.totalPageNum;
    }
    getNoticeBoardList(searchCondition.value.pageNum);


    const goToView = (boardId) => {
      router.push({
        name: 'Notice-View',
        params: {
          id: boardId
        },
        query: {
          startDate: parseStringByFormat(searchCondition.value.startDate, 'YYYY-MM-DD'),
          endDate: parseStringByFormat(searchCondition.value.endDate, 'YYYY-MM-DD'),
          category: searchCondition.value.category,
          searchText: searchCondition.value.searchText,
          pageSize: searchCondition.value.pageSize,
          orderValue: searchCondition.value.orderValue,
          orderDirection: searchCondition.value.orderDirection,
          pageNum: searchCondition.value.pageNum
        }
      })
    }

    return {
      categoryList,
      searchCondition,
      noticeBoardList,
      fixedNoticeBoardList,
      totalPageNum,
      getNoticeBoardList,
      parseStringByFormat,
      goToView,
      isNew,
      truncateTitle
    }
  }
}

</script>

<style scoped>

</style>