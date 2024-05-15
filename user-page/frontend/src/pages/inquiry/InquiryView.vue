<template>
  <Navbar/>
  <v-row>
    <v-col cols="2"></v-col>
    <v-col cols="8">
      <v-card class="card">
        <v-card-title class="board-category">문의 게시판</v-card-title>
        <v-card-subtitle style="margin-bottom: 50px">
          <v-row align="center" class="info-row">
            <v-col cols="12" class="d-flex justify-start align-center">
              <span v-if="answer.answerId" class="title">
                {{ inquiryBoard.title + '(답변완료)' }}
              </span>
              <span v-else class="title">
                {{ inquiryBoard.title + '(미답변)' }}
              </span>
            </v-col>
            <v-col cols="12" class="d-flex align-center">
              <span class="created-at" style="margin-right: 10px">{{
                  parseStringByFormat(inquiryBoard.createdAt, 'YYYY.MM.DD HH:mm')
                }}</span>
              <span class="author-name">{{ inquiryBoard.authorName }}</span>
              <v-spacer></v-spacer>
              <span class="views">조회수 : {{ inquiryBoard.views }}</span>
            </v-col>
          </v-row>
        </v-card-subtitle>
        <v-card-title class="content">{{ inquiryBoard.content }}</v-card-title>
        <v-card-title v-if="answer.answerId" class="answer">
          <div style="margin-bottom: 5px">
            <span class="answer-author">{{ answer.authorName }}</span>
            <span class="answer-created-at">{{
                parseStringByFormat(answer.createdAt, 'YYYY.MM.DD HH:mm')
              }}</span>
          </div>
          <v-divider class="mb-2"></v-divider>
          <span>{{ answer.content }}</span>
        </v-card-title>
        <v-card-title v-else class="content">
          아직 등록된 답변이 없습니다.
        </v-card-title>
      </v-card>
      <v-card-actions style="justify-content: center">
        <v-btn class="custom-btn" @click="goToList">목록</v-btn>
        <v-btn class="custom-btn" v-show="myBoard" @click="goToModify">수정</v-btn>
        <v-btn class="custom-btn" v-show="myBoard" @click="deleteBoard">삭제</v-btn>
      </v-card-actions>
    </v-col>
  </v-row>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import {computed, onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {useStore} from "vuex";
import {
  fetchCheckInquiryAuthor,
  fetchAddInquiryView,
  fetchGetInquiryBoard,
  fetchDeleteInquiryBoard
} from "@/api/inquiryBoardService";

export default {
  components: {Navbar},
  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();
    const boardId = route.params.id;
    const inquiryBoard = ref({});
    const answer = ref({});
    const myBoard = ref(false);
    const accessToken = computed(() => store.getters.getAccessToken);


    watch(accessToken, () => {
      checkAuthor();
    })

    /**
     * 페이지 구성에 필요한 문의 게시판 데이터 로드
     */
    const getBoard = async () => {
      const res = await fetchGetInquiryBoard(boardId);
      await fetchAddInquiryView(boardId);
      inquiryBoard.value = res.inquiryBoard;
      answer.value = res.answer;
    }

    /**
     * 작성자 확인 후 myBoard 세팅
     */
    const checkAuthor = async () => {
      // 비밀글일 때
      try {
        await fetchCheckInquiryAuthor(boardId);
        myBoard.value = true;
      } catch (error) {
        myBoard.value = false;
      }
    }

    /**
     * 게시물 삭제
     */
    const deleteBoard = async () => {
      try {
        await fetchDeleteInquiryBoard(boardId);
        goToList();
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    onMounted(async () => {
      await getBoard();
      await checkAuthor();
    })

    const goToList = () => {
      router.push({
        name: 'Inquiry-List',
        query: route.query
      })
    }

    const goToModify = () => {
      router.push({
        name: 'Inquiry-Modify',
        query: route.query
      })
    }

    return {
      inquiryBoard,
      answer,
      myBoard,
      goToList,
      parseStringByFormat,
      deleteBoard,
      goToModify
    }
  }
}
</script>

<style scoped>
.card {
  margin-top: 70px;
  padding-top: 10px;
  padding-left: 20px;
  padding-right: 20px;
}

.board-category {
  font-size: 40px;
}

.created-at, .author-name, .views {
  font-size: 16px;
  font-weight: 400;
}

.category {
  font-size: 25px;
  font-weight: 500;
  color: black;
}

.title-container {
  min-height: 48px; /* Ensure there is enough height for multiline titles */
}

.title {
  font-weight: 500;
  font-size: 24px;
  max-width: 100%; /* Ensure the title does not overflow the container */
  white-space: pre-wrap;
}

.info-row {
  display: flex;
  align-items: center;
}

.content {
  white-space: pre-wrap;
  margin-bottom: 40px;
  border: 1px solid black;
  padding: 15px;
}

.answer {
  max-width: 100%;
  white-space: pre-wrap;
  border: 1px solid black;
  padding: 15px;
  margin-bottom: 20px;
}

.answer-author {
  margin-right: 10px;
}

.answer-created-at {
  font-weight: 350;
}

.custom-btn {
  margin-top: 30px;
  margin-bottom: 20px;
  background-color: black;
  color: white;
  width: 200px;
}
</style>