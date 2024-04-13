<template>
  <li v-for="page in totalPageNum" :key="page" style="display: inline-flex">
    <span @click="getBoardList(page)">{{ page }}</span>
  </li>
</template>

<script>
import {getCurrentInstance} from "vue";

export default {
  props: {
    totalPageNum: {
      type: Number,
      required: false
    }
  },
  emits: ['click'],
  setup() {
    const {emit} = getCurrentInstance();

    const getBoardList = (page) => {
      emit('click', page);
    }

    return {
      getBoardList
    }
  }
}
</script>

<style scoped>

</style>