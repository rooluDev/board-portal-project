<template>
  <Navbar/>
  <hr/>
  <h1>자유 게시판</h1>
  <SearchForm :categoryList="categoryList" :searchCondition="searchCondition" @search="getFreeBoardList">
    <template v-slot:category>
      <select v-model="searchCondition.category">
        <option value="-1">전체 분류</option>
        <option v-for="category in categoryList" :value="category.categoryId" :key="category.categoryId">
          {{ category.categoryName }}
        </option>
      </select>
    </template>
    <template v-slot:searchBar>
      <input type="text" placeholder="제목 or 내용 or 등록자" v-model="searchCondition.searchText">
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
  <BoardList :boardList="freeBoardList">
    <template v-slot:th>
      <th>번호</th>
      <th>분류</th>
      <th>제목</th>
      <th>조회</th>
      <th>등록일시</th>
      <th>등록자</th>
    </template>
    <template v-slot:td="{item}">
      <td>
        <router-link :to="{name:'Free-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
      </td>
      <td>
        <router-link :to="{name:'Free-View',params:{id:item.boardId}}">{{ item.categoryName }}</router-link>
      </td>
      <td>
        <router-link :to="{name:'Free-View',params:{id:item.boardId}}">{{
            truncateTitle(item.title, 15)
          }}
        </router-link>
        <span v-if="item.commentCount != 0">{{ '(' + item.commentCount + ')' }}</span>
        <span v-if="isNew(item.createdAt, 7)"> new</span>
        <span v-if="item.fileId">$</span>
      </td>
      <td>{{ item.views }}</td>
      <td>{{ parseStringByFormat(item.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
      <td v-if="item.adminName">{{ item.adminName }}</td>
      <td v-if="item.memberName">{{ item.memberName}}</td>
    </template>
  </BoardList>
  <Pagination :totalPageNum="totalPageNum" @click="getFreeBoardList"/>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import BoardList from "@/components/BoardList.vue";
import SearchForm from "@/components/SearchForm.vue";
import Pagination from "@/components/Pagination.vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {ref} from "vue";
import {parseStringByFormat} from "@/utils/SearchConditionUtils";
import router from "@/router";
import {fetchFreeBoardList} from "@/api/freeBoardService";
import {isNew} from "@/utils/DateUtils";
import {truncateTitle} from "@/utils/StringUtils";

export default {
  components: {Navbar, BoardList, SearchForm, Pagination},
  setup() {
    const store = useStore();
    const route = useRoute();
    const accessToken = ref();
    accessToken.value = store.getters.getAccessToken;

    const freeBoardList = ref([]);
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

    const getFreeBoardList = async (pageNum) => {
      searchCondition.value.pageNum = pageNum;
      const res = await fetchFreeBoardList(searchCondition.value);
      categoryList.value = res.categoryList;
      totalPageNum.value = res.totalPageNum;
      freeBoardList.value = res.freeBoardList;
      searchCondition.value = res.searchCondition;
    }
    getFreeBoardList(searchCondition.value.pageNum);

    const goToView = (boardId) => {
      router.push({
        name: 'Free-View',
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
      router.push({
        name: 'Free-Write',
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

    return {
      searchCondition,
      freeBoardList,
      totalPageNum,
      categoryList,
      getFreeBoardList,
      parseStringByFormat,
      goToWrite,
      goToView,
      truncateTitle,
      isNew
    }
  }
}
</script>

<style scoped>

</style>