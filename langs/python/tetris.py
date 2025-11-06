#!/usr/bin/env python3

import pygame


def main():
    pygame.init()
    screen = pygame.display.set_mode((300, 600))  # dubbelbuffrat per default
    clock = pygame.time.Clock()

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        screen.fill((0, 0, 0))
        # rita block här
        
        pygame.draw.rect(screen, (128, 0, 0), (100, 100, 20, 20))
        
        pygame.display.flip()  # byter buffertar
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    main()
