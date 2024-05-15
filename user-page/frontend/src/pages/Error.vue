<template>
  <h3>잘못된 요청입니다</h3>
  <button type="button" @click="goToMain">메인 페이지</button>
</template>
<script>
import {useRouter} from "vue-router";

export default {
  setup() {
    const router = useRouter();

    const goToMain = () => {
      router.push({
        name: 'Main'
      })
    }

    return {
      goToMain
    }
  }
}
</script>
<style scoped>

</style>