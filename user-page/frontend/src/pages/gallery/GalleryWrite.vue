<template>
  <Navbar/>
  <div class="box">
    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <h1 class="mb-4">갤러리</h1>
        <v-form @submit.prevent="writeBoard">
          <v-select
              :items="categoryList"
              item-title="categoryName"
              item-value="categoryId"
              v-model="galleryBoardForm.categoryId"
              solo>
          </v-select>
          <v-text-field
              label="제목"
              placeholder="제목을 입력하세요."
              v-model="galleryBoardForm.title">
          </v-text-field>
          <v-textarea
              placeholder="내용을 입력하세요."
              rows="15"
              v-model="galleryBoardForm.content">
          </v-textarea>
          <h3 class="mb-3">첨부 파일</h3>
          <div class="d-flex" v-for="(fileInput, index) in galleryBoardForm.fileList" :key="index">
            <v-img
                v-show="filePreviews[index]"
                :src="filePreviews[index]"
                :max-width="60"
                :height="60"
                :aspect-ratio="1"
                cover
            ></v-img>
            <v-file-input
                v-model="galleryBoardForm.fileList[index]"
                label="첨부"
                :accept="fileAcceptTypes"
                prepend-icon="none"
                @change="fileSelected($event,index)"
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
    </v-row>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import {Board} from "@/type/boardType";
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchCategoryListByBoardType} from "@/api/categoryService";
import {fetchAddGalleryBoard} from "@/api/galleryBoardService";
import {galleryBoardValidator, isValidFileSize} from "@/validator/validator";

export default {
  components: {Navbar},
  setup() {
    const route = useRoute();
    const router = useRouter();

    const categoryList = ref([{categoryId: -1, categoryName: '카테고리 선택'}]);
    const filePreviews = ref([]);
    const fileAcceptTypes = 'image/jpeg, image/jpg, image/gif, image/png';
    const galleryBoardForm = ref({
      categoryId: -1,
      title: '',
      content: '',
      fileList: []
    })

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
     * 갤러리 게시판 카테고리 리스트 가져오기
     */
    const getCategoryList = async () => {
      try {
        const res = await fetchCategoryListByBoardType(Board.GALLERY_BOARD);
        res.forEach((category) => {
          categoryList.value.push(category);
        })
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    }

    onMounted(() => {
      getCategoryList();
    })

    /**
     * 게시물 추가
     */
    const writeBoard = async () => {
      try {
        galleryBoardForm.value.fileList = galleryBoardForm.value.fileList.filter(file => file != null);

        galleryBoardValidator(galleryBoardForm.value, constraint);

        const formData = new FormData();
        galleryBoardForm.value.fileList.forEach((file) => {
          formData.append('file', file);
        })
        formData.append('categoryId', galleryBoardForm.value.categoryId);
        formData.append('title', galleryBoardForm.value.title);
        formData.append('content', galleryBoardForm.value.content);

        await fetchAddGalleryBoard(formData);
        alert("등록 되었습니다.");
        await router.push({
          name: 'Gallery-List'
        })
      } catch (error) {
        alert(error.message);
      }
    }

    const createdPreviewImage = (index, file) => {
      const reader = new FileReader();
      reader.onload = (e) => {
        filePreviews.value[index] = e.target.result;
      }
      reader.readAsDataURL(file);
    }

    const fileSelected = (event, index) => {
      const file = event.target.files[0];

      const isValidFile = file && isValidFileSize(file.size, 1 * 1024 * 1024);

      if (isValidFile) {
        galleryBoardForm.value.fileList[index] = file;
        createdPreviewImage(index, file);
      } else {
        galleryBoardForm.value.fileList.splice(index, 1);
        filePreviews.value.splice(index, 1);
      }
    }

    const addFileInput = () => {
      if (galleryBoardForm.value.fileList.length < 5) {
        galleryBoardForm.value.fileList.push(null);
      } else {
        alert("파일은 최대 5개까지만 등록 가능합니다.");
      }
    }

    const removeFileInput = (index) => {
      galleryBoardForm.value.fileList.splice(index, 1);
      filePreviews.value.splice(index, 1);
    }

    const goToList = () => {
      router.push({
        name: 'Gallery-List',
        query: route.query
      })
    }

    return {
      galleryBoardForm,
      categoryList,
      filePreviews,
      fileAcceptTypes,
      writeBoard,
      goToList,
      addFileInput,
      fileSelected,
      removeFileInput
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