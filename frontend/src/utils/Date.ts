export const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  date.setHours(date.getHours() + 9); // 한국 표준시로 변환하기 위해 9시간 추가

  const now = new Date();
  now.setHours(now.getHours() + 9); // 현재 시간도 한국 표준시로 변환

  const YY = String(date.getFullYear()).slice(-2);
  const MM = String(date.getMonth() + 1).padStart(2, "0");
  const DD = String(date.getDate()).padStart(2, "0");
  const HH = String(date.getHours()).padStart(2, "0");
  const MI = String(date.getMinutes()).padStart(2, "0");

  // 년, 월, 일이 같으면 오늘로 판단
  if (
    YY === String(now.getFullYear()).slice(-2) &&
    MM === String(now.getMonth() + 1).padStart(2, "0") &&
    DD === String(now.getDate()).padStart(2, "0")
  ) {
    return `${HH}:${MI}`;
  } else {
    return `${YY}-${MM}-${DD}`;
  }
};

export const formatDateDetail = (dateString: string): string => {
  const date = new Date(dateString);
  date.setHours(date.getHours() + 9); // 한국 표준시로 변환하기 위해 9시간 추가

  const now = new Date();
  now.setHours(now.getHours() + 9); // 현재 시간도 한국 표준시로 변환

  const YY = String(date.getFullYear()).slice(-2);
  const MM = String(date.getMonth() + 1).padStart(2, "0");
  const DD = String(date.getDate()).padStart(2, "0");
  const HH = String(date.getHours()).padStart(2, "0");
  const MI = String(date.getMinutes()).padStart(2, "0");

  return `${YY}-${MM}-${DD} ${HH}:${MI}`;
};
