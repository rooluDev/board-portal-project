<template>
  <Navbar/>
  <v-row>
    <v-col cols="2"></v-col>
    <v-col cols="8">
      <v-card class="card">
        <v-card-title class="board-category">공지사항</v-card-title>
        <v-card-subtitle style="margin-bottom: 50px">
          <v-row align="center" class="info-row">
            <v-col cols="12" class="d-flex title-container">
              <span class="category">{{ noticeBoard.categoryName }}</span>
            </v-col>
            <v-col cols="12" class="d-flex justify-start align-center">
              <span class="title">{{ noticeBoard.title }}</span>
            </v-col>
            <v-col cols="12" class="d-flex justify-end align-center">
              <span class="created-at" style="margin-right: 10px">{{
                  parseStringByFormat(noticeBoard.createdAt, 'YYYY.MM.DD HH:mm')
                }}</span>
              <span class="author-name">{{ noticeBoard.authorName }}</span>
              <v-spacer></v-spacer>
              <span class="views">조회수 : {{ noticeBoard.views }}</span>
            </v-col>
          </v-row>
        </v-card-subtitle>
        <v-card-title class="content">{{ noticeBoard.content }}</v-card-title>
      </v-card>
      <v-card-actions style="justify-content: center">
        <v-btn class="custom-btn" @click="goToList">목록</v-btn>
      </v-card-actions>
    </v-col>
    <v-col cols="2"></v-col>
  </v-row>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import {fetchGetNoticeBoard} from "@/api/noticeBoardService";
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchAddNoticeView} from "@/api/noticeBoardService";
import {parseStringByFormat} from "@/utils/searchConditionUtils";

export default {
  components: {Navbar},
  setup() {
    const noticeBoard = ref({});
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;

    /**
     * 페이지 구성을 위한 데이터 로드
     */
    const getBoard = async () => {
      noticeBoard.value = await fetchGetNoticeBoard(boardId);
      await fetchAddNoticeView(boardId);
    }

    onMounted(() => {
      getBoard();
    })

    const goToList = () => {
      router.push({
        name: 'Notice-List',
        query: route.query
      })
    }

    return {
      noticeBoard,
      goToList,
      parseStringByFormat
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
  margin-bottom: 20px;
  border: 1px solid black;
  padding: 15px;
}

.custom-btn {
  margin-top: 30px;
  margin-bottom: 20px;
  background-color: black;
  color: white;
  width: 200px;
}
</style>