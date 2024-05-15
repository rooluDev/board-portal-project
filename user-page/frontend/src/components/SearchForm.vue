<template>

  <v-form @submit.prevent="getNewBoardList" class="box">
    <v-card class="card">
      <v-row>
        <v-col cols="1"></v-col>
        <v-col cols="2">
          <v-text-field
              type="date"
              v-model="$props.searchCondition.startDate">
          </v-text-field>
        </v-col>
        <v-col cols="2">
          <v-text-field
              type="date"
              v-model="$props.searchCondition.endDate">
          </v-text-field>
        </v-col>
        <v-col cols="2" v-if="$props.category">
          <v-select
              label="카테고리 선택"
              :items="addedDefaultCategoryList"
              item-title="categoryName"
              item-value="categoryId"
              v-model="$props.searchCondition.category"
              solo>
          </v-select>
        </v-col>
        <v-col cols="3" v-if="$props.category">
          <v-text-field
              type="text"
              v-model="$props.searchCondition.searchText"
              :placeholder="$props.placeholder">
          </v-text-field>
        </v-col>
        <v-col v-else cols="5">
          <v-text-field
              type="text"
              v-model="$props.searchCondition.searchText"
              :placeholder="$props.placeholder">
          </v-text-field>
        </v-col>
        <v-col cols="2">
          <v-btn class="custom-btn" type="submit">검색</v-btn>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="1"></v-col>
        <v-col cols="2">
          <v-select
              v-model="$props.searchCondition.pageSize"
              :items="[10, 20, 30, 40, 50]"
              label="개씩 보기"
          ></v-select>
        </v-col>
        <v-col cols="4"></v-col>
        <v-col cols="2">
          <v-select
              v-if="inquiry"
              v-model="$props.searchCondition.orderValue"
              :items="[
                  { title: '등록 일시', value: 'createdAt' },
                  { title: '제목', value: 'title' },
                  { title: '조회수', value: 'views' }]"
              label="정렬"
          ></v-select>
          <v-select
              v-else
              v-model="$props.searchCondition.orderValue"
              :items="[
                  { title: '등록 일시', value: 'createdAt' },
                  { title: '분류', value: 'category'},
                  { title: '제목', value: 'title' },
                  { title: '조회수', value: 'views' }]"
              label="정렬"
          ></v-select>
        </v-col>
        <v-col cols="2">
          <v-select
              v-model="$props.searchCondition.orderDirection"
              :items="[
                  {title: '내림차순', value: 'desc'},
                  {title: '오름차순', value: 'asc'}]"
              label="정렬 방향"
          ></v-select>
        </v-col>
      </v-row>
    </v-card>
  </v-form>

</template>

<script>
import {computed, getCurrentInstance, ref, watch} from 'vue';
import {useStore} from "vuex";

export default {
  props: {
    categoryList: {
      type: Array,
      required: false,
    },
    searchCondition: {
      type: Object,
      required: true
    },
    placeholder: {
      type: String,
      required: true
    },
    category: {
      type: Boolean,
      required: true
    },
    inquiry: {
      type: Boolean,
      required: false
    }
  },
  emits: ['search'],
  setup(props) {
    const {emit} = getCurrentInstance();
    const store = useStore();
    const accessToken = computed(() => store.getters.getAccessToken);
    const addedDefaultCategoryList = ref([{categoryId: -1, categoryName: '전체 분류'}]);

    const addDefaultCategory = () => {
      props.categoryList.forEach((category) => {
        addedDefaultCategoryList.value.push(category);
      })
    }

    watch(() => props.categoryList, () => {
      addDefaultCategory();
    })

    watch(() => props.searchCondition.pageSize, () => {
      props.searchCondition.pageNum = 1;
      emit('search', props.searchCondition);
    })

    watch(() => props.searchCondition.orderValue, () => {
      props.searchCondition.pageNum = 1;
      emit('search', props.searchCondition);
    })

    watch(() => props.searchCondition.orderDirection, () => {
      props.searchCondition.pageNum = 1;
      emit('search', props.searchCondition);
    })

    const getNewBoardList = () => {
      console.log(props.searchCondition);
      emit('search', props.searchCondition);
    }

    return {
      accessToken,
      addedDefaultCategoryList,
      getNewBoardList
    }
  }
}
</script>

<style scoped>
.box {
  justify-content: center;
  margin: 50px 100px 10px;
}

.card {
  padding-top: 20px;

}

.custom-btn {
  width: 100px;
  height: 55px;
  font-size: 17px;
  color: white;
  background-color: black;
}
</style>