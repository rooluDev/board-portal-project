<template>
  <h1>로그인</h1>
  <form @submit.prevent="getLogin">
    <input type="text" placeholder="아이디" v-model="memberId">
    <input type="password" placeholder="비밀번호" v-model="password">
    <button type="submit">로그인</button>
    <button type="button" @click="goToJoin">회원가입</button>
  </form>

  <div v-if="errorMessage != ''">
    {{ errorMessage }}
  </div>
</template>

<script>
import {ref} from "vue";
import {useRouter} from 'vue-router';
import {loginService} from '@/api/loginService';
import {useStore} from "vuex";

export default {
  setup() {
    const router = useRouter();
    const store = useStore();
    const memberId = ref('');
    const password = ref('');
    const errorMessage = ref('');

    const getLogin = async () => {
      const accessToken = await loginService(memberId.value, password.value);
      if (accessToken === null) {
        errorMessage.value = '아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.입력하신 내용을 다시 확인해주세요.';
        return;
      }
      // vuex에 저장
      await store.dispatch('storeToken', accessToken);
      // 그 이전 페이지
      const nextRoute = store.getters.getNextRoute;

      if (nextRoute){
        await store.dispatch('deleteNextRoute');
        await router.push(nextRoute);
      }else {
        goToMain();
      }
    }

    const goToJoin = () => {
      router.push({
        name: 'Join'
      })
    }

    const goToMain = () => {
      router.push({
        name:'Main'
      })
    }

    return {
      memberId,
      password,
      errorMessage,
      goToJoin,
      getLogin
    }
  }

}
</script>

<style scoped>

</style>