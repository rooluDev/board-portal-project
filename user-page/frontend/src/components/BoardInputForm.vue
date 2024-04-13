<template>
  <form @submit.prevent="submit">
    <slot name="category"></slot>
    <label>제목*</label>
    <input type="text" placeholder="제목을 입력해 주세요." v-model="inputForm.title">
    <br/>
    <label>내용*</label>
    <textarea v-model="inputForm.content"></textarea>
    <br/>
    <slot name="secret"></slot>
    <slot name="file"></slot>
    <br/>
    <button type="submit">{{ editing ? "수정" : "등록" }}</button>
    <button type="button" @click="cancel">취소</button>
  </form>
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
    },
    categoryList: {
      type:Array,
      required:false
    },
    fileList:{
      type:Array,
      required:false
    }
  },
  emits:['submit', 'cancel'],
  setup(props){
    const {emit} = getCurrentInstance();

    const submit = () => {
      emit('submit', props.inputForm);
    }

    const cancel = () => {
      emit('cancel');
    }

    return{
      submit,
      cancel
    }
  }
}
</script>

<style scoped>

</style>