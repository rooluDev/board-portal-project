<template>
  <v-app-bar :elevation="1" class="d-flex justify-space-between">
    <v-col cols="4">
      <v-app-bar-title @click="goToMain" style="cursor: pointer" class="ma-3">
        포트폴리오
      </v-app-bar-title>
    </v-col>
    <v-col cols="4" class="d-flex justify-center">
      <v-btn text :to="{name: 'Notice-List'}">공지사항</v-btn>
      <v-btn text :to="{name: 'Free-List'}">자유 게시판</v-btn>
      <v-btn text :to="{name: 'Gallery-List'}">갤러리</v-btn>
      <v-btn text :to="{name: 'Inquiry-List'}">문의 게시판</v-btn>
    </v-col>
    <v-col cols="4" class="d-flex justify-end align-center">
      <template v-if="!accessToken">
        <v-btn text @click="goToLogin">로그인</v-btn>
        <v-btn text :to="{name: 'Join'}">회원가입</v-btn>
      </template>
      <template v-else>
        <span class="mr-4">{{ memberName }}님 안녕하세요!</span>
        <v-btn text @click="logout">로그아웃</v-btn>
      </template>
    </v-col>
  </v-app-bar>
</template>

<script>
import {useStore} from "vuex";
import {fetchGetMember} from "@/api/memberService";
import {useRoute} from "vue-router";
import {computed, onMounted, ref} from "vue";
import router from "@/router";

export default {
  setup() {
    const store = useStore();
    const memberName = ref(null);
    const route = useRoute()
    const accessToken = computed(() => store.getters.getAccessToken);

    /**
     * memberName 가져오기
     */
    const getMemberName = async () => {
      if (accessToken.value) {
        const res = await fetchGetMember();
        memberName.value = res.memberName;
      }
    }

    onMounted(() => {
      if (accessToken.value) {
        getMemberName();
      }
    })

    const logout = () => {
      store.dispatch('deleteToken');
    }

    const goToLogin = () => {
      router.push({
        name: 'Login',
        query: {
          ret: route.fullPath
        }
      })
    }

    const goToMain = () => {
      router.push({
        name: 'Main'
      })
    }

    return {
      accessToken,
      memberName,
      logout,
      goToLogin,
      goToMain
    }
  }
}
</script>

<style scoped>
</style>