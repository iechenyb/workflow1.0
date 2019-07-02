var app = new Vue({
  el: '#app',
  data: {
    message: 'Hello Vue!',
    todos: [
            { text: '学习 JavaScript',author:'zhangsan' },
            { text: '学习 Vue' ,author:'lisi'},
            { text: '整个牛项目',author:'wangwu' }
          ]
  }
})