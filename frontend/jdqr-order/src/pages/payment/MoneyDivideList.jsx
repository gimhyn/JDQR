import { Stack } from "@mui/material";
import MoneyDivideInfo from "./MoneyDivideInfo";
import MoneyDivideListItem from "./MoneyDivideListItem";
import BaseButton from "../../components/button/BaseButton";
import { moneyDivide } from "../../utils/apis/order";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const MoneyDivideList = ({ orders }) => {
  const navigate = useNavigate(); // 반드시 함수 호출 형태여야 함
  const dishes = orders.orders.flatMap((order) => order.dishes);
  const [total, setTotal] = useState(orders.userCnt);
  const [portion, setPortion] = useState(1);
  const [money, setMoney] = useState(0);

  const conjoinedDishes = dishes.reduce((acc, dish) => {
    const key = `${dish.dishId}-${dish.options
      ?.map((option) => `${option.optionId}-${option.choiceId}`) // optionId-choiceId 형태로 변환
      .sort() // 옵션을 정렬
      .join("_")}`; // 정렬된 옵션을 병합

    if (acc[key]) {
      acc[key].totalPrice += dish.price * dish.quantity;
      acc[key].quantity += dish.quantity;
    } else {
      acc[key] = {
        key,
        dishId: dish.dishId,
        dishName: dish.dishName,
        price: dish.price,
        totalPrice: dish.price * dish.quantity,
        quantity: dish.quantity,
        dishCategoryId: dish.dishCategoryId,
        dishCategoryName: dish.dishCategoryName,
        options: [...dish.options],
      };
    }

    return acc;
  }, {});

  const conjoinedDishesArray = Object.values(conjoinedDishes);
  const moneyPay = async () => {
    try {
      console.log("portion", portion);
      const response = await moneyDivide({
        peopleNum: total,
        serveNum: portion,
      });

      const { tossOrderId, amount } = response;
      console.log("결제 성공:", response);

      await sendTossOrder({ tossOrderId, value: amount });
    } catch (error) {
      console.error("결제 요청 중 에러 발생:", error);
      alert("결제 요청 중 문제가 발생했습니다. 다시 시도해 주세요.");
    }
  };

  const sendTossOrder = async ({ tossOrderId, value }) => {
    try {
      const orderName = "테스트 주문"; // orderName 정의
      navigate("/toss", {
        state: {
          orderId: tossOrderId,
          value: value,
          orderName,
        },
      });
    } catch (error) {
      console.log("토스 주문하다 에러 처ㅣ", error);
    }
  };

  const handleVaulesChange = ({ total, portion, money }) => {
    setTotal(total);
    setPortion(portion);
    setMoney(money);
  };

  return (
    <Stack>
      <MoneyDivideInfo
        initTotal={orders.userCnt}
        totalPrice={orders.price}
        onValuesChange={handleVaulesChange}
      />
      {conjoinedDishesArray.map((dish) => (
        <MoneyDivideListItem key={dish.key} dish={dish} />
      ))}
      <BaseButton onClick={moneyPay}>
        {money.toLocaleString()}원 결제하기
      </BaseButton>
    </Stack>
  );
};

export default MoneyDivideList;