<template>
  <Navbar/>
  <hr/>
  <h1>문의 게시판</h1>
  <SearchForm :searchCondition="searchCondition" @search="getInquiryBoardList">
    <template v-slot:searchBar>
      <input type="text" placeholder="제목 or 내용 or 등록자" v-model="searchCondition.searchText">
    </template>
    <template v-slot:myInquiry v-if="accessToken">
      <span>나의 문의내역만 보기</span>
      <input type="checkbox">
    </template>
    <template v-slot:orderValue>
      <select v-model="searchCondition.orderValue">
        <option value="createdAt">등록 일시</option>
        <option value="title">제목</option>
        <option value="title">조회수</option>
      </select>
    </template>
  </SearchForm>
  <button type="button" @click="goToWrite">글 등록</button>
  <BoardList :boardList="inquiryBoardList">
    <template v-slot:th>
      <th>번호</th>
      <th>제목</th>
      <th>조회</th>
      <th>등록일시</th>
      <th>등록자</th>
    </template>
    <template v-slot:td="{item}">
      <td>
        <router-link :to="{name:'Inquiry-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
      </td>
      <td>
        <router-link :to="{name:'Inquiry-View',params:{id:item.boardId}}">
            <span v-if="item.answerId">
              {{ truncateTitle(item.title, 10) + '(답변완료)' }}
            </span>
          <span v-else>
              {{ truncateTitle(item.title, 10) + '(미답변)' }}
            </span>
        </router-link>
        <span v-if="isNew(item.createdAt, 7)"> new</span>
        <span v-if="item.secret === '1'">$</span>
      </td>
      <td>{{ item.views }}</td>
      <td>{{ parseStringByFormat(item.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
      <td>{{ item.authorName }}</td>
    </template>
  </BoardList>
  <Pagination :totalPageNum="totalPageNum" @click="getInquiryBoardList"/>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import BoardList from "@/components/BoardList.vue";
import SearchForm from "@/components/SearchForm.vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {ref} from "vue";
import {fetchInquiryBoardList} from "@/api/inquiryBoardService";
import {parseStringByFormat} from "@/utils/SearchConditionUtils";
import router from "@/router";
import Pagination from "@/components/Pagination.vue";
import {truncateTitle} from "@/utils/StringUtils";
import {isNew} from "@/utils/DateUtils";

export default {
  components: {Pagination, Navbar, BoardList, SearchForm},
  setup() {
    const store = useStore();
    const route = useRoute();
    const accessToken = ref();

    accessToken.value = store.getters.getAccessToken;


    const inquiryBoardList = ref([]);
    const totalPageNum = ref(0);
    const searchCondition = ref({
      startDate: route.query.startDate || '',
      endDate: route.query.endDate || '',
      searchText: route.query.searchText || '',
      pageSize: route.query.pageSize || 10,
      orderValue: route.query.orderValue || 'createdAt',
      orderDirection: route.query.orderDirection || 'desc',
      pageNum: route.query.pageNum || 1
    })


    const getInquiryBoardList = async (pageNum) => {
      searchCondition.value.pageNum = pageNum;
      const res = await fetchInquiryBoardList(searchCondition.value);
      totalPageNum.value = res.totalPageNum;
      inquiryBoardList.value = res.inquiryBoardList;
      searchCondition.value = res.searchCondition;
    }

    getInquiryBoardList(searchCondition.value.pageNum);

    const goToView = (boardId) => {
      router.push({
        name: 'Inquiry-View',
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
      if (accessToken.value) {
        router.push({
          name: 'Inquiry-Write',
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
        if (confirm("로그인 하시겠습니까?")) {
          store.dispatch('storeNextRoute', {
            name: 'Inquiry-Write',
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
          router.push({
            name: 'Login'
          })
        }
      }
    }

    return {
      searchCondition,
      inquiryBoardList,
      totalPageNum,
      getInquiryBoardList,
      parseStringByFormat,
      goToView,
      goToWrite,
      truncateTitle,
      isNew,
      accessToken
    }
  }
}
</script>

<style scoped>

</style>