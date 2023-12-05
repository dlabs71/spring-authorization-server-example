import moment from "moment";

moment.locale('ru');

export function formatDate(date) {
    date = date ? new Date(date) : null
    return !!date ? moment(date).format('DD.MM.YYYY') : null;
}

export function formatDateFromStr(date) {
    return moment(date).format('DD.MM.YYYY')
}

export function beautyFormatDate(date) {
    if (!date) {
        return null;
    }
    return moment(date).format('D MMMM YYYY года')
}