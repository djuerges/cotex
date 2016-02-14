/* activate tokenfield for usernames in form */
$('#tokenfield').tokenfield({
    autocomplete: {
        source: ['sfischer', 'aschrader', 'fschmidt', 'aduenniger', 'akoenig',
            'baltakrouri', 'dbimschas', 'dboldt', 'tbusshaus', 'sebers', 'ofarooq',
            'dgregorczyk', 'bgrosse', 'fjalalina', 'okleine', 'ymawad', 'rmietz', 'dpfisterer',
            'wpietsch', 'mwaqasrehan', 'sristock', 'prothenpieler', 'ischumacher', 'kdschumacher',
            'mstelzner', 'twinklehorst'],
        delay: 100
    },
    showAutocompleteOnFocus: true
});