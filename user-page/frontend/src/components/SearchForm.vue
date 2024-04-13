<template>
  <form @submit.prevent="getNewBoardList">
    <label>등록일시</label>
    <input type="date" v-model="searchCondition.startDate">
    <label>~</label>
    <input type="date" v-model="searchCondition.endDate">
    <slot name="category"></slot>
    <slot name="searchBar"></slot>
    <button type="submit">검색</button>
    <br/>
    <slot name="myInquiry"></slot>
    <hr/>
    <select v-model="searchCondition.pageSize">
      <option value="10">10</option>
      <option value="20">20</option>
      <option value="30">30</option>
      <option value="40">40</option>
      <option value="50">50</option>
    </select>
    <span>개씩 보기</span>
    <span>정렬</span>
    <slot name="orderValue"></slot>
    <select v-model="searchCondition.orderDirection">
      <option value="desc">내림차순</option>
      <option value="asc">오름차순</option>
    </select>
  </form>
</template>

<script>
import {getCurrentInstance, ref, watch} from 'vue';
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
    }
  },
  emits: ['search'],
  setup(props) {
    const {emit} = getCurrentInstance();
    const store = useStore();
    const accessToken = ref();

    accessToken.value = store.getters.getAccessToken;

    watch(() => props.searchCondition.pageSize, () => {
      emit('search', props.searchCondition.pageNum);
    })

    watch(() => props.searchCondition.orderValue, () => {
      emit('search', props.searchCondition.pageNum);
    })

    watch(() => props.searchCondition.orderDirection, () => {
      emit('search', props.searchCondition.pageNum);
    })

    const getNewBoardList = () => {
      emit('search', props.searchCondition.pageNum);
    }

    return {
      accessToken,
      getNewBoardList
    }
  }
}
</script>

<style scoped>

</style>