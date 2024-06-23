import moment from 'moment'

function validate(date) {
    return (date && moment(date).isValid)
}

export default {
    methods: {
        formatDate(date) {
            date = date ? new Date(date) : null
            return validate(date) ? moment(date).format('DD.MM.YYYY') : null
        },
        formatDateWithPattern(date, pattern) {
            date = date ? new Date(date) : null
            return validate(date) ? moment(date).format(pattern) : null
        },
        isoFormatDate(date) {
            return validate(date) ? moment(date).format('YYYY-MM-DD') : null
        },
        isoFormatDateTime(date) {
            return validate(date) ? moment(date).format('YYYY-MM-DD HH:mm:ss') : null
        },
        isoFormatLocalDateTime(date) {
            return validate(date) ? moment(date).format('YYYY-MM-DDTHH:mm:ss') : null
        },
        str2Date(value) {
            const parsedDate = moment(value, 'DD.MM.YYYY', true)
            return parsedDate.isValid() ? moment(parsedDate.toDate()) : null
        },
        str2DateWithPattern(value, pattern) {
            const parsedDate = moment(value, pattern, true)
            return parsedDate.isValid() ? moment(parsedDate.toDate()) : null
        },
        currentDate() {
            return moment(new Date()).format('YYYY-MM-DD')
        },
        formatDateTime(dateTime) {
            return dateTime ? moment(new Date(String(dateTime))).format('DD.MM.YYYY HH:mm:ss') : null
        },
        formatDateTimeShort(dateTime) {
            return dateTime ? moment(new Date(String(dateTime))).format('DD.MM.YYYY HH:mm') : null
        },
        isValidStrDate(date) {
            return moment(date, 'DD.MM.YYYY', true).isValid()
        },
    }
}
