<template>
  <form @submit.prevent="addComment">
    <textarea v-model="content" placeholder="댓글을 입력해 주세요."></textarea>
    <button type="submit">등록</button>
  </form>
  <div v-for="(comment,index) in commentList" :key="index">
    <span>{{ comment.adminName }}</span>
    <span>{{ comment.memberName }}</span>
    <span>{{ parseStringByFormat(comment.createdAt, 'YYYY.MM.DD HH:mm') }}</span>
    <br/>
    <span>{{ comment.content }}</span>
  </div>
</template>

<script>
import {parseStringByFormat} from "@/utils/SearchConditionUtils";
import {getCurrentInstance, ref} from "vue";

export default {
  props: {
    commentList: {
      type: Array,
      required: true
    }
  },
  emits: ['submit'],
  setup() {
    const {emit} = getCurrentInstance();
    const content = ref("");

    const addComment = () => {
      emit('submit', content.value);
    }

    return {
      content,
      addComment,
      parseStringByFormat
    }
  }
}
</script>

<style>

</style>