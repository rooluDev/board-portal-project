<template>
  <table>
    <thead>
    <tr>
      <slot name="th"></slot>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(item, index) in fixedNoticeBoardList" :key="index">
      <slot name="fixedTd" :item="item"></slot>
    </tr>
    <tr v-for="(item, index) in boardList" :key="index">
      <slot name="td" :item="item"></slot>
    </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  props: {
    boardList: {
      type: Array,
      required: true
    },
    fixedNoticeBoardList: {
      type: Array,
      required: false
    }
  }
}
</script>

<style scoped>

</style>