<template>
  <v-pagination :length="totalPageNum" @update:model-value="getBoardList"></v-pagination>
</template>

<script>
import {getCurrentInstance} from "vue";

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

    const getBoardList = (page) => {
      props.searchCondition.pageNum = page;
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