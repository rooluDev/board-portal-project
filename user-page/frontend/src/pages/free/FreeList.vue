<template>
  <Navbar/>
  <hr/>
  <div class="box">
    <h1 style="text-align: center">자유 게시판</h1>
    <SearchForm :categoryList="categoryList" :searchCondition="searchCondition" @search="getFreeBoardList"
                placeholder="제목 or 내용 or 등록자" :category="true"/>
    <v-row style="margin-top: 20px">
      <v-col cols="9"></v-col>
      <v-col cols="3">
        <v-btn class="custom-btn" type="button" @click="goToWrite">글 등록</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <v-table>
          <thead>
          <tr>
            <th class="text-center">번호</th>
            <th class="text-center">분류</th>
            <th class="text-left">제목</th>
            <th class="text-center">조회</th>
            <th class="text-center">등록일시</th>
            <th class="text-center">등록자</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="board in freeBoardList" :key="board.boardId">
            <td class="text-center">
              <span class="link" @click="goToView(board.boardId)">
                {{ board.boardId }}
              </span>
            </td>
            <td class="text-left">
              <span class="link" @click="goToView(board.boardId)">{{ board.categoryName }}</span>
            </td>
            <td class="text-left">
              <span class="link" @click="goToView(board.boardId)">
                {{ truncateTitle(board.title, 60) }}
              </span>
              <span v-if="board.commentCount > 0" class="link"
                    @click="goToView(board.boardId)">{{ '(' + board.commentCount + ')' }}</span>
              <span class="new" v-if="isNew(board.createdAt, 7)">new</span>
              <span class="file" v-if="board.fileId">📁</span>
            </td>
            <td class="text-center">{{ board.views }}</td>
            <td class="text-center">{{ parseStringByFormat(board.createdAt, 'YYYY.MM.DD HH:mm') }}</td>
            <td class="text-center">{{ board.adminName || board.memberName }}</td>
          </tr>
          </tbody>
        </v-table>
      </v-col>
      <v-col cols="1"></v-col>
    </v-row>
    <Pagination :searchCondition="searchCondition" :totalPageNum="totalPageNum" @click="getFreeBoardList"/>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import SearchForm from "@/components/SearchForm.vue";
import Pagination from "@/components/Pagination.vue";
import {Board} from "@/type/boardType";
import router from "@/router";
import {computed, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {fetchGetFreeBoardList} from "@/api/freeBoardService";
import {isNew} from "@/utils/dateUtils";
import {parseToQueryString, truncateTitle} from "@/utils/stringUtils";
import {format, subMonths} from "date-fns";
import {useStore} from "vuex";

export default {
  components: {Navbar, SearchForm, Pagination},
  setup() {
    const route = useRoute();
    const store = useStore();
    const accessToken = computed(() => store.getters.getAccessToken);

    const freeBoardList = ref([]);
    const totalPageNum = ref(0);
    const categoryList = ref([]);

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
    const getFreeBoardList = async (searchConditionParam) => {
      const res = await fetchGetFreeBoardList(searchConditionParam);
      categoryList.value = res.categoryList;
      totalPageNum.value = res.totalPageNum;
      freeBoardList.value = res.freeBoardList;
      searchCondition.value = res.searchCondition;
      console.log(freeBoardList.value);
    }

    onMounted(() => {
      getFreeBoardList(searchCondition.value);
    })

    const goToView = (boardId) => {
      router.push({
        name: 'Free-View',
        params: {
          id: boardId
        },
        query: queryObject.value
      })
    }

    const goToWrite = () => {
      if (accessToken.value) {
        router.push({
          name: 'Free-Write',
          query: queryObject.value
        })
      } else {
        if (confirm('로그인 하시겠습니까?')) {
          router.push({
            name: 'Login',
            query: {
              ret: `/boards/free/write${parseToQueryString(queryObject.value, Board.FREE_BOARD)}`,
            }
          })
        }
      }
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

.file {
  margin-left: 10px;
}

.custom-btn {
  margin-left: 95px;
  background-color: black;
  color: white;
}
</style>