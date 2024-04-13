<template>
  <h1>회원가입</h1>
  <form @submit.prevent="getJoin">
    <input type="text" v-model="memberId" placeholder="아이디">
    <button type="button" @click.prevent="getCheckDuplicateMemberId">중복 확인</button>
    <input type="password" v-model="password" placeholder="비밀번호">
    <input type="password" v-model="passwordCheck" placeholder="비밀번호 확인">
    <input type="text" v-model="memberName" placeholder="이름">
    <button type="submit" :disabled="passMemberId">회원 가입 하기</button>
  </form>
</template>

<script>
import {ref} from "vue";
import {joinService, checkDuplicateMemberId} from "@/api/joinService";

export default {
  setup() {
    const memberId = ref('');
    const password = ref('');
    const passwordCheck = ref('');
    const memberName = ref('');

    const passMemberId = ref(true);

    const getCheckDuplicateMemberId = async () => {
      const res = await checkDuplicateMemberId(memberId.value);
      if (res === "success") {
        passMemberId.value = false;
        alert("사용 가능한 ID 입니다.");
      } else {
        alert("중복된 ID 입니다.");
      }
    }

    const getJoin = async () => {
      await joinService(memberId.value, password.value, passwordCheck.value, memberName.value);
    }

    return {
      memberId,
      password,
      passwordCheck,
      memberName,
      passMemberId,
      getCheckDuplicateMemberId,
      getJoin
    }
  }
}
</script>

<style scoped>

</style>