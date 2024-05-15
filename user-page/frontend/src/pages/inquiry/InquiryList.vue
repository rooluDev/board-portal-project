<template>
  <Navbar/>
  <hr/>
  <div class="box">
    <h1 style="text-align: center">문의 게시판</h1>
    <SearchForm :searchCondition="searchCondition" @search="getInquiryBoardList" :placeholder="'제목 or 내용 or 등록자'"
                :inquiry="true"
                :category="false"/>
    <v-row style="margin-top: 20px">
      <v-col cols="1"></v-col>
      <v-col cols="8">
        <v-checkbox
            v-model="searchCondition.my"
            v-if="accessToken"
            label="나의 문의내역 보기"
            @change="getMyInquiryBoardList"
        >
        </v-checkbox>
      </v-col>
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
            <th class="text-left">제목</th>
            <th class="text-center">조회</th>
            <th class="text-center">등록일시</th>
            <th class="text-center">등록자</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="board in inquiryBoardList" :key="board.boardId">
            <td class="text-center">
              <span class="link" @click="goToView(board.boardId, board.isSecret)">{{ board.boardId }}</span>
            </td>
            <td class=" text-left">
              <div>
                <span class="link" @click="goToView(board.boardId,board.isSecret)">{{
                    truncateTitle(board.title, 60)
                  }}</span>
                <span v-if="board.answerId">(답변완료)</span>
                <span v-else>(미답변)</span>
                <span class="new" v-if="isNew(board.createdAt, 7)">new</span>
                <span v-if="board.isSecret === '1'">🔒</span>
              </div>
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
    <Pagination :searchCondition="searchCondition" :totalPageNum="totalPageNum" @click="getInquiryBoardList"/>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import SearchForm from "@/components/SearchForm.vue";
import router from "@/router";
import Pagination from "@/components/Pagination.vue";
import {computed, onMounted, ref} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {fetchCheckInquiryAuthor, fetchGetInquiryBoardList} from "@/api/inquiryBoardService";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {parseToQueryString, truncateTitle} from "@/utils/stringUtils";
import {isNew} from "@/utils/dateUtils";
import {format, subMonths} from "date-fns";
import {Board} from "@/type/boardType";

export default {
  components: {Pagination, Navbar, SearchForm},
  setup() {
    const store = useStore();
    const route = useRoute();
    const accessToken = computed(() => store.getters.getAccessToken);

    const inquiryBoardList = ref([]);
    const totalPageNum = ref(0);
    const searchCondition = ref({
      startDate: route.query.startDate || format(subMonths(new Date(), 1), 'yyyy-MM-dd'),
      endDate: route.query.endDate || format(new Date(), 'yyyy-MM-dd'),
      searchText: route.query.searchText || '',
      pageSize: route.query.pageSize || 10,
      orderValue: route.query.orderValue || 'createdAt',
      orderDirection: route.query.orderDirection || 'desc',
      pageNum: route.query.pageNum || 1,
      my: route.query.my === 'true'
    })

    const queryObject = computed(() => ({
          startDate: searchCondition.value.startDate,
          endDate: searchCondition.value.endDate,
          searchText: searchCondition.value.searchText,
          pageSize: searchCondition.value.pageSize,
          orderValue: searchCondition.value.orderValue,
          orderDirection: searchCondition.value.orderDirection,
          pageNum: searchCondition.value.pageNum,
          my: searchCondition.value.my
        })
    )
    /**
     * 페이지에 필요한 데이터 로드
     *
     * @param searchConditionParam 검색조건
     */
    const getInquiryBoardList = async (searchConditionParam) => {
      try {
        const res = await fetchGetInquiryBoardList(searchConditionParam);
        totalPageNum.value = res.totalPageNum;
        inquiryBoardList.value = res.inquiryBoardList;
        searchCondition.value = res.searchCondition;
        searchCondition.value.my = searchCondition.value.my === 'true' ? true : false;
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    const getMyInquiryBoardList = async () => {
      await getInquiryBoardList(searchCondition.value);
    }


    onMounted(() => {
      getInquiryBoardList(searchCondition.value);
    })

    const goToView = async (boardId, secret) => {
      // 비밀글일 경우
      if (secret == 1) {
        // 작성자 확인
        try {
          await fetchCheckInquiryAuthor(boardId);
          await router.push({
            name: 'Inquiry-View',
            params: {id: boardId},
            query: queryObject.value
          })
        } catch (error) {
          alert("비밀글입니다.");
          return;
        }
      } else {
        await router.push({
          name: 'Inquiry-View',
          params: {id: boardId},
          query: queryObject.value
        })
      }
    }

    const goToWrite = () => {
      // 로그인 확인
      if (accessToken.value) {
        router.push({
          name: 'Inquiry-Write',
          query: queryObject.value
        })
      } else {
        if (confirm("로그인 하시겠습니까?")) {
          router.push({
            name: 'Login',
            query: {
              ret: `/boards/inquiry/write${parseToQueryString(queryObject.value, Board.INQUIRY_BOARD)}`,
            }
          })
        }
      }
    }

    return {
      searchCondition,
      inquiryBoardList,
      totalPageNum,
      queryObject,
      getInquiryBoardList,
      parseStringByFormat,
      goToView,
      goToWrite,
      truncateTitle,
      isNew,
      accessToken,
      getMyInquiryBoardList
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

.custom-btn {
  margin-left: 95px;
  background-color: black;
  color: white;
}
</style>