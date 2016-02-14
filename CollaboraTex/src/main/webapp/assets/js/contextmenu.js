/* 
 * Daniel Jürges <djuerges@googlemail.com>
 */

(function($, window) {

    $.fn.contextMenu = function(settings) {

        return this.each(function() {

            // Open context menu
            $(this).on("contextmenu", function(e) {
                //open menu
                $(settings.menuSelector)
                        .data("invokedOn", $(e.target))
                        .show()
                        .css({
                            position: "absolute",
                            left: getLeftLocation(e),
                            top: getTopLocation(e)
                        });

                //add click listener on menu
                ContextMenuClickHandler();

                return false;
            });

            // click handler for context menu
            function ContextMenuClickHandler() {
                $(settings.menuSelector)
                        .off('click')
                        .on('click', function(e) {
                            $(this).hide();

                            var $invokedOn = $(this).data("invokedOn");
                            var $selectedMenu = $(e.target);

                            settings.menuSelected.call($(this), $invokedOn, $selectedMenu);
                        });

            }

            //make sure menu closes on any click
            $(document).click(function() {
                $(settings.menuSelector).hide();
            });
        });

        /* calculate left location so menu will not overlap the viewport and be partially hidden */
        function getLeftLocation(e) {
            var mouseWidth = e.pageX;
            var pageWidth = $(window).width();
            var menuWidth = $(settings.menuSelector).width();

            // opening menu would pass the side of the page
            if (mouseWidth + menuWidth > pageWidth &&
                    menuWidth < mouseWidth) {
                return mouseWidth - menuWidth;
            }
            return mouseWidth;
        }

        /* calculate top location so menu will not overlap the viewport and be partially hidden */
        function getTopLocation(e) {
            var mouseHeight = e.pageY;
            var pageHeight = $(window).height();
            var menuHeight = $(settings.menuSelector).height();

            // opening menu would pass the bottom of the page
            if (mouseHeight + menuHeight > pageHeight &&
                    menuHeight < mouseHeight) {
                return mouseHeight - menuHeight;
            }
            return mouseHeight;
        }

    };
})(jQuery, window);

/* set all context menus on side ready */
$(document).ready(setSidebarContextMenu());
$(document).ready(setProjectContextMenu());
$(document).ready(setFileContextMenu());

function setSidebarContextMenu() {
    $(".sidebar").contextMenu({
        menuSelector: "#sidebarContextMenu",
        menuSelected: function(invokedOn, selectedMenu) {
            /* nothing to do here, in clickhandling.js register click handlers do all the work */
        }
    });
}

function setProjectContextMenu() {
    $(".nav-sidebar>li.active").contextMenu({
        menuSelector: "#projectContextMenu",
        menuSelected: function(invokedOn, selectedMenu) {
            /* get project id */
            var projectId = invokedOn.attr('data-pk');

            /* get name of the project */
            var name = invokedOn.text().trim();

            /* choose right action */
            switch (selectedMenu.attr('id')) {
                case 'renameproject':
                    renameProject(projectId, name);
                    break;
                case 'downloadproject':
                    downloadProject(projectId);
                    break;
                case 'deleteproject':
                    deleteProject(projectId);
                    break;
                case 'newfile':
                    createNewFile(projectId);
                    break;
            }
        }
    });
}

function setFileContextMenu() {
    $(".nav-sidebar>li:not(.active)").contextMenu({
        menuSelector: "#fileContextMenu",
        menuSelected: function(invokedOn, selectedMenu) {
            /* get file id */
            var file = invokedOn.parent('li');
            var fileId = invokedOn.attr("data-pk");

            /* get project id */
            var project = file.siblings('li.active');
            var projectId = project.children('a').attr('data-pk');

            /* get name of the file */
            var name = invokedOn.text().trim();

            /* choose right action */
            switch (selectedMenu.attr('id')) {
                case 'renamefile':
                    renameFile(projectId, fileId, name);
                    break;
                case 'setmaintexfile':
                    setFileAsMainTex(projectId, fileId);
                    break;
                case 'deletefile':
                    deleteProjectFile(projectId, fileId);
                    break;
            }
        }
    });
}