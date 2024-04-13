<template>
  <h1>포트폴리오</h1>
  <Navbar/>
  <hr/>
  <div>
    <BoardTitleBar>
      <template v-slot:board-category>
        <span>공지사항</span>
      </template>
      <template v-slot:more>
        <router-link :to="{name:'Notice-List'}">더보기 +</router-link>
      </template>
    </BoardTitleBar>
    <BoardList :boardList="noticeBoardList">
      <template v-slot:th>
        <th>번호</th>
        <th>분류</th>
        <th>제목</th>
      </template>
      <template v-slot:td="{ item }">
        <td>
          <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{ item.categoryName }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Notice-View',params:{id:item.boardId}}">{{
              truncateTitle(item.title, 5)
            }}
          </router-link>
          <span v-if="isNew(item.createdAt, 7)"> new</span>
        </td>
      </template>
    </BoardList>
  </div>
  <div>
    <BoardTitleBar>
      <template v-slot:board-category>
        <span>자유 게시판</span>
      </template>
      <template v-slot:more>
        <router-link :to="{name:'Free-List'}">더보기 +</router-link>
      </template>
    </BoardTitleBar>
    <BoardList :boardList="freeBoardList">
      <template v-slot:th>
        <th>번호</th>
        <th>분류</th>
        <th>제목</th>
      </template>
      <template v-slot:td="{ item }">
        <td>
          <router-link :to="{name:'Free-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Free-View',params:{id:item.boardId}}">{{ item.categoryName }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Free-View',params:{id:item.boardId}}">{{
              truncateTitle(item.title, 5)
            }}
          </router-link>
          <span v-if="item.commentCount != 0">{{ '(' + item.commentCount + ')' }}</span>
          <span v-if="isNew(item.createdAt, 7)"> new</span>
          <span v-if="item.fileId">$</span>
        </td>
      </template>
    </BoardList>
  </div>
  <div>
    <BoardTitleBar>
      <template v-slot:board-category>
        <span>갤러리</span>
      </template>
      <template v-slot:more>
        <router-link :to="{name:'Gallery-List'}">더보기 +</router-link>
      </template>
    </BoardTitleBar>
    <BoardList :boardList="galleryBoardList">
      <template v-slot:th>
        <th>번호</th>
        <th>분류</th>
        <th></th>
      </template>
      <template v-slot:td="{ item }">
        <td>
          <router-link :to="{name:'Gallery-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Gallery-View',params:{id:item.boardId}}">{{ item.categoryName }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Gallery-View',params:{id:item.boardId}}">
            <img :src="`${item.filePath}/${item.physicalName}.${item.extension}`"/>
          </router-link>
          <span v-if="item.fileCount > 1">{{ '+' + item.fileCount }}</span>
          <span v-if="isNew(item.createdAt, 7)"> new</span>
        </td>
      </template>
    </BoardList>
  </div>
  <div>
    <BoardTitleBar>
      <!--      TODO : 나의 문의 내역-->
      <template v-slot:board-category>
        <span>문의 게시판</span>
      </template>
      <template v-slot:more>
        <router-link :to="{name:'Inquiry-List'}">더보기 +</router-link>
      </template>
    </BoardTitleBar>
    <BoardList :boardList="inquiryBoardList">
      <template v-slot:th>
        <th>번호</th>
        <th>제목</th>
      </template>
      <template v-slot:td="{ item }">
        <td>
          <router-link :to="{name:'Inquiry-View',params:{id:item.boardId}}">{{ item.boardId }}</router-link>
        </td>
        <td>
          <router-link :to="{name:'Inquiry-View',params:{id:item.boardId}}">
            <span v-if="item.answerId">
              {{ truncateTitle(item.title, 5) + '(답변완료)' }}
            </span>
            <span v-else>
              {{ truncateTitle(item.title, 5) + '(미답변)' }}
            </span>
          </router-link>
          <span v-if="isNew(item.createdAt, 7)"> new</span>
          <span v-if="item.secret === '1'">$</span>
        </td>
      </template>
    </BoardList>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';
import BoardTitleBar from '@/components/BoardTitleBar.vue';
import BoardList from '@/components/BoardList.vue';
import {useStore} from "vuex";
import {boardListForMain} from "@/api/freeBoardService";
import {ref} from "vue";
import {isNew} from "@/utils/DateUtils";
import {truncateTitle} from "@/utils/StringUtils";

export default {
  components: {
    Navbar,
    BoardTitleBar,
    BoardList
  },
  setup() {
    const store = useStore();
    const accessToken = store.getters.getAccessToken;
    const boardList = ref([]);
    const freeBoardList = ref([]);
    const noticeBoardList = ref([]);
    const galleryBoardList = ref([]);
    const inquiryBoardList = ref([]);

    const getBoardList = async () => {
      boardList.value = await boardListForMain();
      freeBoardList.value = boardList.value.freeBoardList;
      noticeBoardList.value = boardList.value.noticeBoardList;
      galleryBoardList.value = boardList.value.galleryBoardList;
      inquiryBoardList.value = boardList.value.inquiryBoardList;
      console.log(inquiryBoardList.value);
    }
    getBoardList();

    // const goToNoticeView = (boardId) => {
    //   router.push({
    //     name: 'Notice-View',
    //     params: {
    //       id: boardId
    //     }
    //   })
    // }

    return {
      accessToken,
      boardList,
      freeBoardList,
      galleryBoardList,
      inquiryBoardList,
      noticeBoardList,
      isNew,
      truncateTitle
    }
  }
}
</script>

<style scoped>

</style>