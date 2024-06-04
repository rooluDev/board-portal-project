<template>
  <Navbar/>
  <hr/>
  <div class="box">
    <h1 style="text-align: center">갤러리</h1>
    <SearchForm :categoryList="categoryList" :searchCondition="searchCondition" @search="getGalleryBoardList"
                placeholder="제목 or 내용" :category="true"/>
    <v-row style="margin-top: 20px">
      <v-col cols="9"></v-col>
      <v-col cols="3">
        <v-btn class="custom-btn" type="button" @click="goToWrite">글 등록</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <v-row v-for="board in galleryBoardList" :key="board.boardId">
          <v-col cols="12">
            <v-card>
              <v-row>
                <v-col cols="3" class="d-flex align-center">
                  <v-img
                      :src="imageUrls[board.boardId]"
                      height="180px"
                      aspect-ratio="1/1"
                  ></v-img>
                </v-col>
                <v-col cols="9" class="mt-2">
                  <v-card-title class="headline link" @click="goToView(board.boardId)">
                    {{ truncateText(board.title, 40) }}
                    <span class="new" style="color: red" v-if="isNew(board.createdAt,12)">new</span>
                  </v-card-title>
                  <v-card-text @click="goToView(board.boardId)">
                    {{ truncateText(board.content, 470) }}
                  </v-card-text>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="1"></v-col>
    </v-row>
    <Pagination :searchCondition="searchCondition" :totalPageNum="totalPageNum" @click="getGalleryBoardList"/>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import SearchForm from "@/components/SearchForm.vue";
import Pagination from "@/components/Pagination.vue";
import router from "@/router";
import {computed, onMounted, ref} from "vue";
import {Board} from "@/type/boardType";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {isNew} from "@/utils/dateUtils";
import {fetchGetGalleryBoardList} from "@/api/galleryBoardService";
import {fetchGetThumbnailResource} from "@/api/imgaeService";
import {format, subMonths} from "date-fns";
import {parseToQueryString, truncateText} from "@/utils/stringUtils";

export default {
  components: {Navbar, SearchForm, Pagination},
  setup() {
    const store = useStore();
    const route = useRoute();
    const accessToken = computed(() => store.getters.getAccessToken);

    const galleryBoardList = ref([]);
    const totalPageNum = ref(0);
    const categoryList = ref([]);
    const imageUrls = ref({});

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
     * 썸네일 이미지 가져오기
     */
    const getThumbnailResource = async () => {
      for (const board of galleryBoardList.value) {
        try {
          const imageBlob = await fetchGetThumbnailResource(board.thumbnailId);
          imageUrls.value[board.boardId] = URL.createObjectURL(imageBlob);
        } catch (error) {
          imageUrls.value[board.boardId] = null;
        }
      }
    };

    /**
     * 페이지 구성을 위한 데이터 로드
     *
     * @param searchConditionParam 검색조건
     */
    const getGalleryBoardList = async (searchConditionParam) => {
      try {
        const res = await fetchGetGalleryBoardList(searchConditionParam);
        categoryList.value = res.categoryList;
        totalPageNum.value = res.totalPageNum;
        searchCondition.value = res.searchCondition;
        galleryBoardList.value = res.galleryBoardList;
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    onMounted(async () => {
      await getGalleryBoardList(searchCondition.value);
      await getThumbnailResource();
    })


    const goToView = (boardId) => {
      router.push({
        name: 'Gallery-View',
        params: {
          id: boardId
        },
        query: queryObject.value
      })
    }

    const goToWrite = () => {
      if (accessToken.value) {
        router.push({
          name: 'Gallery-Write',
          query: queryObject.value
        })
      } else {
        if (confirm('로그인 하시겠습니까?')) {
          router.push({
            name: 'Login',
            query: {
              ret: `/boards/gallery/write${parseToQueryString(queryObject.value, Board.GALLERY_BOARD)}`,
            }
          })
        }
      }
    }

    return {
      searchCondition,
      totalPageNum,
      categoryList,
      galleryBoardList,
      imageUrls,
      truncateText,
      getGalleryBoardList,
      parseStringByFormat,
      goToView,
      goToWrite,
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

.custom-btn {
  margin-left: 95px;
  background-color: black;
  color: white;
}

.link {
  cursor: pointer;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.new {
  margin-left: 20px;
}
</style>