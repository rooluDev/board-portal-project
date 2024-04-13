<template>
  <slot name="boardName"></slot>
  <slot name="category"></slot>
  <span>{{ board.title }}</span>
  <slot name="answer"></slot>
  <span>{{ parseStringByFormat(board.createdAt, 'YYYY-MM-DD HH:mm') }}</span>
  <span>{{ board.authorName }}</span>
  <span>{{ board.adminName }}</span>
  <span>{{ board.memberName }}</span>
  <hr/>
  <span>조회수 : {{ board.views }}</span>
  <slot name="img"></slot>
  <div>{{ board.content }}</div>
</template>

<script>
import {parseStringByFormat} from "@/utils/SearchConditionUtils";

export default {
  props: {
    board: {
      type: Object,
      required: true
    }
  },
  setup() {

    return {
      parseStringByFormat
    }
  }
}
</script>

<style scoped>

</style>