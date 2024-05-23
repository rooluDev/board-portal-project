<template>
  <div class="box">
    <Navbar/>
    <v-row>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="d-flex justify-space-between">
            <span class="justify-start">공지사항</span>
            <v-btn class="justify-end custom-btn" text :to="{name: 'Notice-List'}">더보기 +</v-btn>
          </v-card-title>
          <v-card-text>
            <v-table>
              <thead>
              <tr>
                <th class="text-left">번호</th>
                <th class="text-left">분류</th>
                <th class="text-left">제목</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="board in noticeBoardList" :key="board.boardId">
                <td>
                  <span class="link" @click="goToNoticeView(board.boardId)">
                    {{ board.boardId }}
                  </span>
                </td>
                <td>{{ board.categoryName }}</td>
                <td>
                  <span class="link" @click="goToNoticeView(board.boardId)">
                    {{ truncateText(board.title, 25) }}
                  </span>
                  <span class="ml-2" style="color: red" v-if="isNew(board.createdAt, 7)">new</span>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="d-flex justify-space-between">
            <span class="justify-start">자유 게시판</span>
            <v-btn class="justify-end custom-btn" text :to="{name: 'Free-List'}">더보기 +</v-btn>
          </v-card-title>
          <v-card-text>
            <v-table>
              <thead>
              <tr>
                <th class="text-left">번호</th>
                <th class="text-left">분류</th>
                <th class="text-left">제목</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="board in freeBoardList" :key="board.boardId">
                <td>
                  <span class="link" @click="goToFreeView(board.boardId)">
                    {{ board.boardId }}
                  </span>
                </td>
                <td>{{ board.categoryName }}</td>
                <td>
                  <span class="link" @click="goToFreeView(board.boardId)">
                    {{ truncateText(board.title, 45) }}
                  </span>
                  <span v-if="board.commentCount > 0" class="ml-2">
                    {{'(' + board.commentCount + ')' }}
                  </span>
                  <span class="ml-2" style="color: red" v-if="isNew(board.createdAt, 7)">new</span>
                  <span class="ml-2" v-if="board.fileId">📁</span>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="d-flex justify-space-between">
            <span class="justify-start">갤러리</span>
            <v-btn class="justify-end custom-btn" text :to="{name: 'Gallery-List'}">더보기 +</v-btn>
          </v-card-title>
          <v-card-text>
            <v-table>
              <thead>
              <tr>
                <th class="text-left">번호</th>
                <th class="text-left">분류</th>
                <th class="text-left"></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="board in galleryBoardList" :key="board.boardId" class="g-tr">
                <td>
                  <span class="link" @click="goToGalleryView(board.boardId)">
                    {{ board.boardId }}
                  </span>
                </td>
                <td>
                  <span class="link" @click="goToGalleryView(board.boardId)">
                    {{ board.categoryName }}
                  </span>
                </td>
                <td>
                  <div class="td-g-img">
                    <img class="link" :src="imageUrls[board.boardId]" width="100px" height="70px"
                         @click="goToGalleryView(board.boardId)"/>
                    <span class="ml-4" v-if="board.fileCount > 1">{{ '+' + (board.fileCount - 1) }}</span>
                    <span class="ml-2" style="color: red" v-if="isNew(board.createdAt,7)">new</span>
                  </div>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="d-flex justify-space-between">
            <span class="justify-start">문의 게시판</span>
            <div class="justify-end">
              <v-btn class="custom-btn" text @click="goToMyInquiryList">나의 문의 내역</v-btn>
              <v-btn class="custom-btn ml-2" text :to="{name: 'Inquiry-List'}">더보기 +</v-btn>
            </div>
          </v-card-title>
          <v-card-text>
            <v-table>
              <thead>
              <tr>
                <th class="text-left">번호</th>
                <th class="text-left">제목</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="board in inquiryBoardList" :key="board.boardId">
                <td>
                  <span class="link" @click="goToInquiryView(board.boardId, board.isSecret)">{{ board.boardId }}</span>
                </td>
                <td>
                  <span v-if="board.answerId" class="link"
                        @click="goToInquiryView(board.boardId, board.isSecret)">
                    {{ truncateText(board.title, 40) + ' (답변완료)' }}
                  </span>
                  <span v-else class="link" @click="goToInquiryView(board.boardId, board.isSecret)">
                    {{ truncateText(board.title, 40) + ' (미답변)' }}
                  </span>
                  <span class="ml-2" style="color: red" v-if="isNew(board.createdAt, 7)">new</span>
                  <span class="ml-2" v-if="board.isSecret === '1'">🔒</span>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';
import router from "@/router";
import {computed, onMounted, ref} from "vue";
import {useStore} from "vuex";
import {fetchGetBoardListForMain} from "@/api/boardsService";
import {fetchGetThumbnailResource} from "@/api/imgaeService";
import {fetchCheckInquiryAuthor} from "@/api/inquiryBoardService";
import {isNew} from "@/utils/dateUtils";
import {truncateText} from "@/utils/stringUtils";

export default {
  components: {
    Navbar
  },
  setup() {
    const store = useStore();
    const freeBoardList = ref([]);
    const noticeBoardList = ref([]);
    const galleryBoardList = ref([]);
    const inquiryBoardList = ref([]);
    const imageUrls = ref({});
    const accessToken = computed(() => store.getters.getAccessToken);

    /**
     * 메인 페이지에 필요한 데이터 로드
     */
    const getBoardListForMain = async () => {
      try {
        const res = await fetchGetBoardListForMain();
        freeBoardList.value = res.freeBoardList;
        noticeBoardList.value = res.noticeBoardList;
        galleryBoardList.value = res.galleryBoardList;
        inquiryBoardList.value = res.inquiryBoardList;
        await getImageResource();
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    /**
     * 갤러리 썸네일 이미지 리소스 로드
     */
    const getImageResource = async () => {
      for (const board of galleryBoardList.value) {
        try {
          const imageBlob = await fetchGetThumbnailResource(board.thumbnailId);
          imageUrls.value[board.boardId] = URL.createObjectURL(imageBlob);
        } catch (error) {
          imageUrls.value[board.boardId] = null;
        }
      }
    };

    onMounted(getBoardListForMain);

    const goToNoticeView = (boardId) => {
      router.push({
        name: 'Notice-View',
        params: {
          id: boardId
        }
      })
    }

    const goToFreeView = (boardId) => {
      router.push({
        name: 'Free-View',
        params: {
          id: boardId
        }
      })
    }

    const goToGalleryView = (boardId) => {
      router.push({
        name: 'Gallery-View',
        params: {
          id: boardId
        }
      })
    }


    const goToInquiryView = async (boardId, isSecret) => {
      // 비밀글일 시
      if (isSecret == 1) {
        // 자신이 쓴 글인지 확인
        try {
          await fetchCheckInquiryAuthor(boardId);
          await router.push({
            name: 'Inquiry-View',
            params: {
              id: boardId
            }
          });
        } catch (error) {
          alert("비밀글입니다.");
        }
      } else {
        // 비밀글 아닐 때
        await router.push({
          name: 'Inquiry-View',
          params: {
            id: boardId
          }
        });
      }
    }


    const goToMyInquiryList = () => {
      if (accessToken.value) {
        router.push({
          name: 'Inquiry-List',
          query: {
            my: 'true'
          }
        })
      } else {
        if (confirm("로그인 하시겠습니까?")) {
          router.push({
            name: 'Login',
            query: {
              ret: '/boards/inquiry?my=true'
            }
          })
        }
      }
    }

    return {
      accessToken,
      freeBoardList,
      galleryBoardList,
      inquiryBoardList,
      noticeBoardList,
      imageUrls,
      isNew,
      truncateText,
      goToInquiryView,
      goToMyInquiryList,
      goToGalleryView,
      goToNoticeView,
      goToFreeView
    }
  }
}
</script>

<style scoped>
.box {
  margin: 50px;
}

.link {
  cursor: pointer;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.custom-btn {
  background-color: black;
  color: white;
}

.g-tr {
  height: 105px;
}

.td-g-img {
  display: flex;
  align-items: center;
}
</style>