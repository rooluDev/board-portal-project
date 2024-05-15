<template>
  <div class="box">
    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <h1 class="mb-4">문의 게시판</h1>
        <v-form @submit.prevent="submit">
          <v-text-field
              label="제목"
              placeholder="제목을 입력하세요."
              v-model="$props.inputForm.title">
          </v-text-field>
          <v-textarea
              class="content"
              placeholder="내용을 입력하세요."
              rows="15"
              v-model="$props.inputForm.content">
          </v-textarea>
          <v-checkbox
              label="비밀글"
              placeholder="내용을 입력하세요."
              v-model="$props.inputForm.isSecret">
          </v-checkbox>
          <div class=" d-flex justify-center align-center">
            <v-btn class="custom-btn" type="button" @click.prevent="cancel">취소</v-btn>
            <v-btn class="custom-btn" type="submit">{{ editing ? "수정" : "등록" }}</v-btn>
          </div>
        </v-form>
      </v-col>
      <v-col cols="1"></v-col>
    </v-row>
  </div>
</template>

<script>
import {getCurrentInstance} from "vue";

export default {
  props: {
    editing: {
      type: Boolean,
      required: true
    },
    inputForm: {
      type: Object,
      required: true
    }
  },
  emits: ['submit', 'cancel'],
  setup(props) {
    const {emit} = getCurrentInstance();

    const submit = () => {
      emit('submit', props.inputForm);
    }

    const cancel = () => {
      alert("작성을 취소하시겠습니까?");
      emit('cancel');
    }

    return {
      submit,
      cancel
    }
  }
}
</script>

<style scoped>
.box {
  margin: 50px 100px 10px;
}

.custom-btn {
  margin: 30px 10px 20px;
  background-color: black;
  color: white;
  width: 200px;
}
</style>