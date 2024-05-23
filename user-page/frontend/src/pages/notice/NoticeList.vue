<template>
  <Navbar/>
  <hr/>
  <div class="box">
    <h1 style="text-align: center">공지사항</h1>
    <SearchForm :categoryList="categoryList" :searchCondition="searchCondition" @search="getNoticeBoardList"
                :placeholder="'제목 or 내용'" :category="true"/>
    <v-row class="mt-12">
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <v-table>
          <thead>
          <tr>
            <th class="text-center">번호</th>
            <th class="text-center">분류</th>
            <th style="width: 800px" class="title-left">제목</th>
            <th class="text-center">조회</th>
            <th class="text-center">등록일시</th>
            <th class="text-center">등록자</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="fixedBoard in fixedNoticeBoardList" :key="fixedBoard.boardId" style="background-color: rgba(211, 211, 211, 1)">
            <td></td>
            <td class="text-center">
              <span class="link" @click="goToView(fixedBoard.boardId)">
                {{ fixedBoard.categoryName }}
              </span>
            </td>
            <td class="title-left" style="width: 800px">
              <span class="link" @click="goToView(fixedBoard.boardId)">
                {{ truncateText(fixedBoard.title, 60) }}
              </span>
              <span class="new" v-if="isNew(fixedBoard.createdAt, 7)">new</span>
            </td>
            <td class="text-center">{{ fixedBoard.views }}</td>
            <td class="text-center">{{ parseStringByFormat(fixedBoard.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
            <td class="text-center">{{ fixedBoard.authorName }}</td>
          </tr>
          <tr v-for="board in noticeBoardList" :key="board.boardId">
            <td class="text-center">
              <span class="link" @click="goToView(board.boardId)">
                {{ board.boardId }}
              </span>
            </td>
            <td class="text-center">
              <span class="link" @click="goToView(board.boardId)">
                {{ board.categoryName }}
              </span>
            </td>
            <td class="title-left" style="width: 800px">
              <span class="link" @click="goToView(board.boardId)">
                {{ truncateText(board.title, 60) }}
              </span>
              <span class="new" v-if="isNew(board.createdAt, 7)">new</span>
            </td>
            <td class="text-center">{{ board.views }}</td>
            <td class="text-center">{{ parseStringByFormat(board.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
            <td class="text-center">{{ board.authorName }}</td>
          </tr>
          </tbody>
        </v-table>
      </v-col>
      <v-col cols="1"></v-col>
    </v-row>
    <Pagination :searchCondition="searchCondition" :totalPageNum="totalPageNum" @click="getNoticeBoardList"/>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import SearchForm from "@/components/SearchForm.vue";
import Pagination from "@/components/Pagination.vue";
import router from "@/router";
import {computed, onMounted, ref} from "vue";
import {fetchGetNoticeBoardList} from "@/api/noticeBoardService";
import {useRoute} from "vue-router";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {isNew} from "@/utils/dateUtils";
import {truncateText} from "@/utils/stringUtils";
import {format, subMonths} from "date-fns";

export default {
  components: {Navbar, SearchForm, Pagination},
  setup() {
    const route = useRoute();

    const noticeBoardList = ref([]);
    const fixedNoticeBoardList = ref([]);
    const categoryList = ref([]);
    const totalPageNum = ref(0);

    // 검색조건 초기 설정 및 검색조건 유지를 위한 쿼리스트링을 통한 설정
    const searchCondition = ref({
      startDate: route.query.startDate || format(subMonths(new Date(), 1), 'yyyy-MM-dd'),
      endDate: route.query.endDate || format(new Date(), 'yyyy-MM-dd'),
      category: route.query.category || -1,
      searchText: route.query.searchText || '',
      pageSize: route.query.pageSize || 10,
      orderValue: route.query.orderValue || 'createdAt',
      orderDirection: route.query.orderDirection || 'desc',
      pageNum: route.query.pageNum || 1
    });

    const queryObject = computed(() => ({
      startDate: searchCondition.value.startDate,
      endDate: searchCondition.value.endDate,
      category: searchCondition.value.category,
      searchText: searchCondition.value.searchText,
      pageSize: searchCondition.value.pageSize,
      orderValue: searchCondition.value.orderValue,
      orderDirection: searchCondition.value.orderDirection,
      pageNum: searchCondition.value.pageNum
    }))

    /**
     * 페이지 구성을 위한 데이터 로드
     *
     * @param searchConditionParam 검색조건
     */
    const getNoticeBoardList = async (searchConditionParam) => {
      try {
        const res = await fetchGetNoticeBoardList(searchConditionParam);
        categoryList.value = res.categoryList;
        searchCondition.value = res.searchCondition;
        noticeBoardList.value = res.noticeBoardList;
        fixedNoticeBoardList.value = res.fixedNoticeBoardList;
        totalPageNum.value = res.totalPageNum;
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    onMounted(() => {
      getNoticeBoardList(searchCondition.value);
    });

    const goToView = (boardId) => {
      router.push({
        name: 'Notice-View',
        params: {
          id: boardId
        },
        query: queryObject.value
      })
    }

    return {
      categoryList,
      searchCondition,
      noticeBoardList,
      fixedNoticeBoardList,
      totalPageNum,
      queryObject,
      getNoticeBoardList,
      parseStringByFormat,
      goToView,
      isNew,
      truncateText
    }
  }
}

</script>

<style scoped>
.box {
  justify-content: center;
  margin: 50px 100px 10px;
}

.link {
  cursor: pointer;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.new {
  margin-left: 10px;
  color: red;
}
</style>