<template>
  <v-container class="container">
    <v-row justify="center">
      <v-col cols="12">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark>
            <v-toolbar-title>회원가입</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form @submit.prevent="getJoin">
              <v-text-field
                  label="아이디"
                  prepend-icon="mdi-account"
                  v-model="joinForm.memberId">
              </v-text-field>
              <span>{{ confirmMessage }}</span>
              <v-text-field
                  label="비밀번호"
                  prepend-icon="mdi-lock"
                  v-model="joinForm.password"
                  :type="'password'">
              </v-text-field>
              <v-text-field
                  label="비밀번호 확인"
                  prepend-icon="mdi-lock"
                  v-model="joinForm.passwordCheck"
                  :type="'password'">
              </v-text-field>
              <v-text-field
                  label="이름"
                  prepend-icon="mdi-account"
                  v-model="joinForm.memberName">
              </v-text-field>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" type="submit">회원 가입 하기</v-btn>
              </v-card-actions>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import {watch, ref} from "vue";
import {fetchAddMember, fetchCheckDuplicateMemberId} from "@/api/joinService";
import router from "@/router";

export default {
  setup() {
    const joinForm = ref({
      memberId: '',
      memberName: '',
      password: '',
      passwordCheck: ''
    })

    const isDuplicated = ref(true);
    const confirmMessage = ref('');

    watch(joinForm.value, () => {
      checkDuplicateMemberId();
    })

    /**
     * memberId 중복검사
     */
    const checkDuplicateMemberId = async () => {
      try {
        await fetchCheckDuplicateMemberId(joinForm.value.memberId);
        isDuplicated.value = true;
        confirmMessage.value = "사용할 수 없는 아이디입니다.";
      } catch (error) {
        isDuplicated.value = false;
        confirmMessage.value = "사용할 수 있는 아이디입니다.";
      }
    }

    /**
     * 회원가입
     */
    const getJoin = async () => {
      try {
        await fetchAddMember(joinForm.value);
        alert("회원가입이 되었습니다.");
        await router.push({
          name: 'Main'
        })
      } catch (error) {
        alert("오류");
      }
    }

    return {
      joinForm,
      isDuplicated,
      checkDuplicateMemberId,
      getJoin,
      confirmMessage
    }
  }
}
</script>

<style scoped>
.container {
  margin-top: 50px;
}
</style>