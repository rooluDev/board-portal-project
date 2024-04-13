<template>
  <router-link :to="{name:'Notice-List'}">공지사항</router-link>
  <span> | </span>
  <router-link :to="{name:'Free-List'}">자유 게시판</router-link>
  <span> | </span>
  <router-link :to="{name:'Gallery-List'}">갤러리</router-link>
  <span> | </span>
  <router-link :to="{name:'Inquiry-List'}">문의 게시판</router-link>
  <div v-if="!accessToken" style="display: inline-flex">
    <router-link :to="{name: 'Login'}">로그인</router-link>
    <span> / </span>
    <router-link :to="{name: 'Join'}">회원가입</router-link>
  </div>
  <div v-else style="display: inline-flex">
    <span>{{ memberName }}님 안녕하세요!</span>
    <span @click="logout">로그아웃</span>
  </div>
</template>

<script>
import {useStore} from "vuex";
import {ref} from "vue";
import {fetchMemberName} from "@/api/memberService";

export default {
  setup() {
    const store = useStore();
    const accessToken = ref();
    const memberName = ref();

    accessToken.value = store.getters.getAccessToken;

    const logout = () => {
      store.dispatch('deleteToken');
    }

    const getMemberName = async () => {
      if (accessToken.value) {
        memberName.value = await fetchMemberName(accessToken.value);
      }
    }
    getMemberName();

    return {
      accessToken,
      memberName,
      logout
    }
  }
}
</script>

<style scoped>

</style>