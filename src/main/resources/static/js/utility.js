/* Utility functions */

function formatString(template, ...values) {
    return template.replace(/{(\d+)}/g, (match, index) => values[index]);
}