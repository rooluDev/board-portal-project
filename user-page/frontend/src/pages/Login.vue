<template>
  <v-container class="container">
    <v-row justify="center">
      <v-col cols="20" sm="18" md="6">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark>
            <v-toolbar-title>로그인</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form @submit.prevent="getLogin">
              <v-text-field
                  label="아이디"
                  prepend-icon="mdi-account"
                  v-model="loginForm.memberId"
                  required
              ></v-text-field>
              <v-text-field
                  label="비밀번호"
                  prepend-icon="mdi-lock"
                  v-model="loginForm.password"
                  :type="'password'"
                  required
              ></v-text-field>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" type="submit">로그인</v-btn>
                <v-btn color="grey" @click="goToJoin">회원가입</v-btn>
              </v-card-actions>
            </v-form>
          </v-card-text>
          <v-card-text v-if="errorMessage">
            <v-alert type="error" dismissible>{{ errorMessage }}</v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>


<script>
import {ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import {fetchLogin} from '@/api/loginService';
import {useStore} from "vuex";

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();
    const errorMessage = ref('');
    const loginForm = ref({
      memberId: '',
      password: ''
    })

    const getLogin = async () => {
      const accessToken = await fetchLogin(loginForm.value);
      if (accessToken) {
        await store.dispatch('storeToken', accessToken);
        await router.push(route.query.ret);
      } else {
        errorMessage.value = '아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.입력하신 내용을 다시 확인해주세요.';
      }
    }

    const goToJoin = () => {
      router.push({
        name: 'Join'
      })
    }

    return {
      loginForm,
      errorMessage,
      goToJoin,
      getLogin
    }
  }

}
</script>

<style scoped>
.container{
  margin-top: 50px;
}
</style>