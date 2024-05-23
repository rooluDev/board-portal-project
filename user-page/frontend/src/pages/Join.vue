<template>
  <v-container class="container">
    <v-row justify="center">
      <v-col cols="2"></v-col>
      <v-col cols="8">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark>
            <v-toolbar-title>회원가입</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form @submit.prevent="join">
              <v-text-field
                  label="아이디"
                  prepend-icon="mdi-account"
                  v-model="joinForm.memberId">
              </v-text-field>
              <div style="margin-left: 40px; margin-bottom: 20px">{{ confirmMessage }}</div>
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
                <v-btn :disabled="isDuplicated" class="custom-btn" type="submit">회원 가입 하기</v-btn>
              </v-card-actions>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="2"></v-col>
    </v-row>
  </v-container>
</template>

<script>
import {watch, ref} from "vue";
import {fetchAddMember, fetchCheckDuplicateMemberId} from "@/api/joinService";
import {joinValidator} from "@/validator/validator";
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
    const constraint = {
      memberId: {
        minLength: 4,
        maxLength: 12
      },
      memberName: {
        minLength: 2,
        maxLength: 5
      },
      password: {
        minLength: 4,
        maxLength: 12
      }
    }

    watch(joinForm.value, () => {
      checkDuplicateMemberId();
    })

    /**
     * memberId 중복검사
     */
    const checkDuplicateMemberId = async () => {
      try {
        if (joinForm.value.memberId) {
          await fetchCheckDuplicateMemberId(joinForm.value.memberId);
          isDuplicated.value = false;
          confirmMessage.value = "사용할 수 있는 아이디입니다.";
        } else {
          isDuplicated.value = true;
          confirmMessage.value = "사용할 수 없는 아이디입니다.";
        }
      } catch (error) {
        isDuplicated.value = true;
        confirmMessage.value = "사용할 수 없는 아이디입니다.";
      }
    }

    /**
     * 회원가입
     */
    const join = async () => {
      try {
        joinValidator(joinForm.value, constraint);
        await fetchAddMember(joinForm.value);
        alert("회원가입이 되었습니다.");
        await router.push({
          name: 'Main'
        })
      } catch (error) {
        alert(error.message);
      }
    }

    return {
      joinForm,
      isDuplicated,
      checkDuplicateMemberId,
      join,
      confirmMessage
    }
  }
}
</script>

<style scoped>
.container {
  margin-top: 50px;
}

.custom-btn {
  background-color: black;
  color: white;
  width: 120px;
}
</style>