<template>
  <v-container class="fill-height" fluid>
    <v-row justify="center" align="center">
      <v-col cols="12" md="8">
        <v-card class="pa-5 text-center">
          <v-card-title>
            <h3 class="headline mb-0">잘못된 요청입니다</h3>
          </v-card-title>
          <v-card-text>
            <p>요청하신 페이지를 찾을 수 없습니다. 메인 페이지로 돌아가세요.</p>
          </v-card-text>
          <v-card-actions class="justify-center">
            <v-btn color="primary" @click="goToMain">메인 페이지</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { useRouter } from "vue-router";

export default {
  setup() {
    const router = useRouter();

    const goToMain = () => {
      router.push({
        name: 'Main'
      });
    };

    return {
      goToMain
    };
  }
};
</script>

<style scoped>
.fill-height {
  height: 100vh;
}
</style>
