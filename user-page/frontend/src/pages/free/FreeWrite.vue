<template>
  <Navbar/>
  <div class="box">
    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <h1 class="mb-4">자유 게시판</h1>
        <v-form @submit.prevent="writeBoard">
          <v-select
              :items="categoryList"
              item-title="categoryName"
              item-value="categoryId"
              v-model="freeBoardForm.categoryId"
              solo>
          </v-select>
          <v-text-field
              label="제목"
              placeholder="제목을 입력하세요."
              v-model="freeBoardForm.title">
          </v-text-field>
          <v-textarea
              placeholder="내용을 입력하세요."
              rows="15"
              v-model="freeBoardForm.content">
          </v-textarea>
          <h3 class="mb-3">첨부 파일</h3>
          <div class="d-flex" v-for="(fileInput, index) in freeBoardForm.fileList" :key="index">
            <v-file-input
                v-model="freeBoardForm.fileList[index]"
                label="첨부"
                @change="fileSelected($event,index)"
                :accept="fileAcceptTypes"
            ></v-file-input>
            <v-btn class="ml-2" style="height: 55px" @click="removeFileInput(index)">삭제</v-btn>
          </div>
          <v-btn class="d-block mb-4" @click="addFileInput">추가</v-btn>
          <div class=" d-flex justify-center align-center">
            <v-btn class="custom-btn" type="button" @click="goToList">취소</v-btn>
            <v-btn class="custom-btn" type="submit">등록</v-btn>
          </div>
        </v-form>
      </v-col>
      <v-col cols="1"></v-col>
    </v-row>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import {Board} from "@/type/boardType";
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchCategoryListByBoardType} from "@/api/categoryService";
import {fetchAddFreeBoard} from "@/api/freeBoardService";
import {freeBoardValidator, isValidFileSize} from "@/validator/validator";

export default {
  components: {Navbar},
  setup() {
    const route = useRoute();
    const router = useRouter();

    const categoryList = ref([{categoryId: -1, categoryName: '카테고리 선택'}]);
    const freeBoardForm = ref({
      categoryId: -1,
      title: '',
      content: '',
      fileList: []
    })

    const fileAcceptTypes = 'image/jpeg, image/jpg, image/gif, image/png, application/zip';

    const constraint = {
      title: {
        maxLength: 99,
        minLength: 1
      },
      content: {
        maxLength: 3999,
        minLength: 1
      }
    }


    /**
     * 자유게시판 카테고리 리스트 가져오기
     */
    const getCategoryList = async () => {
      try {
        const res = await fetchCategoryListByBoardType(Board.FREE_BOARD);
        res.forEach((category) => {
          categoryList.value.push(category);
        })
      } catch (error) {
        console.log(error);
      }
    }

    onMounted(() => {
      getCategoryList();
    })

    /**
     * 게시물 추가
     */
    const writeBoard = async () => {
      if (!confirm("등록 하시겠습니까?")){
        return;
      }
      // 유효성 검증
      try {
        freeBoardForm.value.fileList = freeBoardForm.value.fileList.filter(file => file != null);

        freeBoardValidator(freeBoardForm.value, constraint);

        const formData = new FormData();
        freeBoardForm.value.fileList.forEach((file) => {
          formData.append('file', file);
        })
        formData.append('categoryId', freeBoardForm.value.categoryId);
        formData.append('title', freeBoardForm.value.title);
        formData.append('content', freeBoardForm.value.content);
        await fetchAddFreeBoard(formData);
        alert("등록 되었습니다.");
        await router.push({
          name: 'Free-List'
        })
      } catch (error) {
        alert(error.message);
      }
    }

    const fileSelected = (event, index) => {
      const file = event.target.files[0];

      // 파일 사이즈 유효성 검증
      const isValidFile = file && isValidFileSize(file.size, 2 * 1024 * 1024);

      if (isValidFile) {
        freeBoardForm.value.fileList[index] = file;
      } else {
        freeBoardForm.value.fileList.splice(index, 1);
      }
    }

    const addFileInput = () => {
      if (freeBoardForm.value.fileList.length < 5) {
        freeBoardForm.value.fileList.push(null);
      } else {
        // 파일 5개 이상 등록 유효성 검증
        alert("파일은 최대 5개까지만 등록 가능합니다.");
      }
    }

    const removeFileInput = (index) => {
      freeBoardForm.value.fileList.splice(index, 1);
    }

    const goToList = () => {
      if (confirm('작성을 취소하시겠습니까?')) {
        router.push({
          name: 'Free-List',
          query: route.query
        })
      }
    }

    return {
      freeBoardForm,
      categoryList,
      fileAcceptTypes,
      writeBoard,
      goToList,
      removeFileInput,
      addFileInput,
      fileSelected
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