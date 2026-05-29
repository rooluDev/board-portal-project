<template>
  <v-pagination
      v-if="totalPageNum > 0"
      :length="totalPageNum"
      :model-value="Number(searchCondition.pageNum)"
      @update:model-value="getBoardList"
  ></v-pagination>
</template>

<script>
import {getCurrentInstance} from "vue";
import {useRoute, useRouter} from "vue-router";

export default {
  props: {
    totalPageNum: {
      type: Number,
      required: false
    },
    searchCondition: {
      type: Object,
      required: true
    }
  },
  emits: ['click'],
  setup(props) {
    const {emit} = getCurrentInstance();
    const router = useRouter();
    const route = useRoute();

    const getBoardList = (page) => {
      props.searchCondition.pageNum = page;
      // URL을 현재 페이지로 동기화 (뒤로가기 시 올바른 페이지 유지)
      router.replace({query: {...route.query, pageNum: page}});
      emit('click', props.searchCondition);
    }

    return {
      getBoardList
    }
  }
}
</script>

<style scoped>

</style>