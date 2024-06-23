import moment from "moment";

moment.locale('ru');

export function formatDate(date) {
    date = date ? new Date(date) : null
    return !!date ? moment(date).format('DD.MM.YYYY') : null;
}

export function formatDateFromStr(date) {
    if (!date) {
        return null;
    }
    return moment(date).format('DD.MM.YYYY')
}

export function formatDateTimeFromStr(date) {
    if (!date) {
        return null;
    }
    return moment(date).format('DD.MM.YYYY HH:mm');
}

export function beautyFormatDate(date) {
    if (!date) {
        return null;
    }
    return moment(date).locale('ru').format('D MMMM YYYY года')
}